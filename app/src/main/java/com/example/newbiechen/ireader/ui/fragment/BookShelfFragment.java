package com.example.newbiechen.ireader.ui.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.RxBus;
import com.example.newbiechen.ireader.event.DeleteResponseEvent;
import com.example.newbiechen.ireader.event.DeleteTaskEvent;
import com.example.newbiechen.ireader.event.DownloadMessage;
import com.example.newbiechen.ireader.event.RecommendBookEvent;
import com.example.newbiechen.ireader.model.bean.CollBookBean;
import com.example.newbiechen.ireader.model.local.BookRepository;
import com.example.newbiechen.ireader.presenter.BookShelfPresenter;
import com.example.newbiechen.ireader.presenter.contract.BookShelfContract;
import com.example.newbiechen.ireader.ui.activity.ReadActivity;
import com.example.newbiechen.ireader.ui.adapter.CollBookAdapter;
import com.example.newbiechen.ireader.ui.base.BaseMVPFragment;
import com.example.newbiechen.ireader.utils.RxUtils;
import com.example.newbiechen.ireader.utils.ToastUtils;
import com.example.newbiechen.ireader.widget.adapter.WholeAdapter;
import com.example.newbiechen.ireader.widget.itemdecoration.DividerItemDecoration;
import com.example.newbiechen.ireader.widget.refresh.ScrollRefreshRecyclerView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Created by newbiechen on 17-4-15.
 */

public class BookShelfFragment extends BaseMVPFragment<BookShelfContract.Presenter>
        implements BookShelfContract.View {
    private static final String TAG = "BookShelfFragment";
    @BindView(R.id.book_shelf_rv_content)
    ScrollRefreshRecyclerView mRvContent;

    /************************************/
    private CollBookAdapter mCollBookAdapter;
    private FooterItemView mFooterItem;

    //????????????????????????
    private boolean isInit = true;

    @Override
    protected int getContentId() {
        return R.layout.fragment_bookshelf;
    }

    @Override
    protected BookShelfContract.Presenter bindPresenter() {
        return new BookShelfPresenter();
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    private void setUpAdapter() {
        //??????Footer
        mCollBookAdapter = new CollBookAdapter();
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mRvContent.setAdapter(mCollBookAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        //????????????
        Disposable recommendDisp = RxBus.getInstance()
                .toObservable(RecommendBookEvent.class)
                .subscribe(
                        event -> {
                            mRvContent.startRefresh();
                            mPresenter.loadRecommendBooks(event.sex);
                        }
                );
        addDisposable(recommendDisp);

        Disposable donwloadDisp = RxBus.getInstance()
                .toObservable(DownloadMessage.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        event -> {
                            //??????Toast??????
                            ToastUtils.show(event.message);
                        }
                );
        addDisposable(donwloadDisp);

        //???????????? (???????????????)
        Disposable deleteDisp = RxBus.getInstance()
                .toObservable(DeleteResponseEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        event -> {
                            if (event.isDelete) {
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("???????????????");
                                progressDialog.show();
                                BookRepository.getInstance().deleteCollBookInRx(event.collBook)
                                        .compose(RxUtils::toSimpleSingle)
                                        .subscribe(
                                                (Void) -> {
                                                    mCollBookAdapter.removeItem(event.collBook);
                                                    progressDialog.dismiss();
                                                }
                                        );
                            } else {
                                //????????????Dialog
                                AlertDialog tipDialog = new AlertDialog.Builder(getContext())
                                        .setTitle("????????????????????????")
                                        .setMessage("?????????????????????????????????")
                                        .setPositiveButton("??????", (dialog, which) -> {
                                            dialog.dismiss();
                                        }).create();
                                tipDialog.show();
                            }
                        }
                );
        addDisposable(deleteDisp);

        mRvContent.setOnRefreshListener(
                () -> mPresenter.updateCollBooks(mCollBookAdapter.getItems())
        );

        mCollBookAdapter.setOnItemClickListener(
                (view, pos) -> {
                    //????????????????????????????????????????????????????????????
                    CollBookBean collBook = mCollBookAdapter.getItem(pos);
                    if (collBook.isLocal()) {
                        //id???????????????????????????
                        String path = collBook.getCover();
                        File file = new File(path);
                        //????????????????????????????????????
                        if (file.exists() && file.length() != 0) {
                            Log.e("FileInfo", mCollBookAdapter.getItem(pos).toString());
                            ReadActivity.startActivity(getContext(),
                                    mCollBookAdapter.getItem(pos), true);

                        } else {
                            String tip = getContext().getString(R.string.nb_bookshelf_book_not_exist);
                            //??????(??????????????????????????????)
                            new AlertDialog.Builder(getContext())
                                    .setTitle(getResources().getString(R.string.nb_common_tip))
                                    .setMessage(tip)
                                    .setPositiveButton(getResources().getString(R.string.nb_common_sure),
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    deleteBook(collBook);
                                                }
                                            })
                                    .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
                                    .show();
                        }
                    } else {
                        ReadActivity.startActivity(getContext(),
                                mCollBookAdapter.getItem(pos), true);
                    }
                }
        );

        mCollBookAdapter.setOnItemLongClickListener(
                (v, pos) -> {
                    //??????Dialog,????????????Dialog,??????AlterDialog
                    openItemDialog(mCollBookAdapter.getItem(pos));
                    return true;
                }
        );
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mRvContent.startRefresh();
    }

    private void openItemDialog(CollBookBean collBook) {
        String[] menus;
        if (collBook.isLocal()) {
            menus = getResources().getStringArray(R.array.nb_menu_local_book);
        } else {
            menus = getResources().getStringArray(R.array.nb_menu_net_book);
        }
        AlertDialog collBookDialog = new AlertDialog.Builder(getContext())
                .setTitle(collBook.getTitle())
                .setAdapter(new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_list_item_1, menus),
                        (dialog, which) -> onItemMenuClick(menus[which], collBook))
                .setNegativeButton(null, null)
                .setPositiveButton(null, null)
                .create();

        collBookDialog.show();
    }

    private void onItemMenuClick(String which, CollBookBean collBook) {
        switch (which) {
            //??????
            case "??????":
                break;
            //??????
            case "??????":
                //2. ?????????????????????CollBean???????????????????????????????????????Task????????????Service?????????
                //3. ???????????????finish?????????isUpdate???true??????????????????chapter????????????
                //4. ???????????????finish?????????isUpdate???false???
                downloadBook(collBook);
                break;
            //??????
            case "??????":
                deleteBook(collBook);
                break;
            //????????????
            case "????????????":
                break;
            default:
                break;
        }
    }

    private void downloadBook(CollBookBean collBook) {
        //????????????
        mPresenter.createDownloadTask(collBook);
    }

    /**
     * ????????????????????????
     *
     * @param collBook
     */
    private void deleteBook(CollBookBean collBook) {
        if (collBook.isLocal()) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.dialog_delete, null);
            CheckBox cb = (CheckBox) view.findViewById(R.id.delete_cb_select);
            new AlertDialog.Builder(getContext())
                    .setTitle("????????????")
                    .setView(view)
                    .setPositiveButton(getResources().getString(R.string.nb_common_sure), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean isSelected = cb.isSelected();
                            if (isSelected) {
                                ProgressDialog progressDialog = new ProgressDialog(getContext());
                                progressDialog.setMessage("???????????????");
                                progressDialog.show();
                                //??????
                                File file = new File(collBook.getCover());
                                if (file.exists()) file.delete();
                                BookRepository.getInstance().deleteCollBook(collBook);
                                BookRepository.getInstance().deleteBookRecord(collBook.get_id());

                                //???Adapter?????????
                                mCollBookAdapter.removeItem(collBook);
                                progressDialog.dismiss();
                            } else {
                                BookRepository.getInstance().deleteCollBook(collBook);
                                BookRepository.getInstance().deleteBookRecord(collBook.get_id());
                                //???Adapter?????????
                                mCollBookAdapter.removeItem(collBook);
                            }
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.nb_common_cancel), null)
                    .show();
        } else {
            RxBus.getInstance().post(new DeleteTaskEvent(collBook));
        }
    }

    /*******************************************************************8*/

    @Override
    public void showError() {

    }

    @Override
    public void complete() {
        if (mCollBookAdapter.getItemCount() > 0 && mFooterItem == null) {
            mFooterItem = new FooterItemView();
            mCollBookAdapter.addFooterView(mFooterItem);
        }

        if (mRvContent.isRefreshing()) {
            mRvContent.finishRefresh();
        }
    }

    @Override
    public void finishRefresh(List<CollBookBean> collBookBeans) {
        mCollBookAdapter.refreshItems(collBookBeans);
        //?????????????????????????????????????????????
        if (isInit) {
            isInit = false;
            mRvContent.post(
                    () -> mPresenter.updateCollBooks(mCollBookAdapter.getItems())
            );
        }
    }

    @Override
    public void finishUpdate() {
        //?????????????????????????????????
        mCollBookAdapter.refreshItems(BookRepository
                .getInstance().getCollBooks());
    }

    @Override
    public void showErrorTip(String error) {
        mRvContent.setTip(error);
        mRvContent.showTip();
    }

    /*****************************************************************/
    class FooterItemView implements WholeAdapter.ItemView {
        @Override
        public View onCreateView(ViewGroup parent) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.footer_book_shelf, parent, false);
            view.setOnClickListener(
                    (v) -> {
                        //??????RxBus??????
                    }
            );
            return view;
        }

        @Override
        public void onBindView(View view) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.refreshCollBooks();
    }
}
