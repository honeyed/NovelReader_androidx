package com.example.newbiechen.ireader.ui.activity;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newbiechen.ireader.R;
import com.example.newbiechen.ireader.model.bean.packages.SearchBookPackage;
import com.example.newbiechen.ireader.presenter.SearchPresenter;
import com.example.newbiechen.ireader.presenter.contract.SearchContract;
import com.example.newbiechen.ireader.ui.adapter.KeyWordAdapter;
import com.example.newbiechen.ireader.ui.adapter.SearchBookAdapter;
import com.example.newbiechen.ireader.ui.base.BaseMVPActivity;
import com.example.newbiechen.ireader.widget.RefreshLayout;
import com.example.newbiechen.ireader.widget.itemdecoration.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by newbiechen on 17-4-24.
 */

public class SearchActivity extends BaseMVPActivity<SearchContract.Presenter>
        implements SearchContract.View{
    private static final String TAG = "SearchActivity";
    private static final int TAG_LIMIT = 8;

    @BindView(R.id.search_iv_back)
    ImageView mIvBack;
    @BindView(R.id.search_et_input)
    EditText mEtInput;
    @BindView(R.id.search_iv_delete)
    ImageView mIvDelete;
    @BindView(R.id.search_iv_search)
    ImageView mIvSearch;
    @BindView(R.id.search_book_tv_refresh_hot)
    TextView mTvRefreshHot;
    @BindView(R.id.search_tg_hot)
    TagGroup mTgHot;
/*    @BindView(R.id.search_rv_history)
    RecyclerView mRvHistory;*/
    @BindView(R.id.refresh_layout)
    RefreshLayout mRlRefresh;
    @BindView(R.id.refresh_rv_content)
    RecyclerView mRvSearch;

    private KeyWordAdapter mKeyWordAdapter;
    private SearchBookAdapter mSearchAdapter;

    private boolean isTag;
    private List<String> mHotTagList;
    private int mTagStart = 0;

    @Override
    protected int getContentId() {
        return R.layout.activity_search;
    }

    @Override
    protected SearchContract.Presenter bindPresenter() {
        return new SearchPresenter();
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setUpAdapter();
        mRlRefresh.setBackground(ContextCompat.getDrawable(this,R.color.white));
    }

    private void setUpAdapter(){
        mKeyWordAdapter = new KeyWordAdapter();
        mSearchAdapter = new SearchBookAdapter();

        mRvSearch.setLayoutManager(new LinearLayoutManager(this));
        mRvSearch.addItemDecoration(new DividerItemDecoration(this));
    }

    @Override
    protected void initClick() {
        super.initClick();

        //??????
        mIvBack.setOnClickListener(
                (v) -> onBackPressed()
        );

        //?????????
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().equals("")){
                    //??????delete??????????????????????????????
                    if (mIvDelete.getVisibility() == View.VISIBLE){
                        mIvDelete.setVisibility(View.INVISIBLE);
                        mRlRefresh.setVisibility(View.INVISIBLE);
                        //??????????????????
                        mKeyWordAdapter.clear();
                        mSearchAdapter.clear();
                        mRvSearch.removeAllViews();
                    }
                    return;
                }
                //??????delete??????
                if (mIvDelete.getVisibility() == View.INVISIBLE){
                    mIvDelete.setVisibility(View.VISIBLE);
                    mRlRefresh.setVisibility(View.VISIBLE);
                    //???????????????????????????
                    mRlRefresh.showFinish();
                }
                //??????
                String query = s.toString().trim();
                if (isTag){
                    mRlRefresh.showLoading();
                    mPresenter.searchBook(query);
                    isTag = false;
                }
                else {
                    //??????
                    mPresenter.searchKeyWord(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //???????????????
        mEtInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //?????????????????????
                if(keyCode==KeyEvent.KEYCODE_ENTER) {
                    searchBook();
                    return true;
                }
                return false;
            }
        });

        //????????????
        mIvSearch.setOnClickListener(
                (v) -> searchBook()
        );

        //?????????
        mIvDelete.setOnClickListener(
                (v) ->  {
                    mEtInput.setText("");
                    toggleKeyboard();
                }
        );

        //????????????
        mKeyWordAdapter.setOnItemClickListener(
                (view, pos) -> {
                    //??????????????????
                    mRlRefresh.showLoading();
                    String book = mKeyWordAdapter.getItem(pos);
                    mPresenter.searchBook(book);
                    toggleKeyboard();
                }
        );

        //Tag???????????????
        mTgHot.setOnTagClickListener(
                tag -> {
                    isTag = true;
                    mEtInput.setText(tag);
                }
        );

        //Tag???????????????
        mTvRefreshHot.setOnClickListener(
                (v) -> refreshTag()
        );

        //?????????????????????
        mSearchAdapter.setOnItemClickListener(
                (view, pos) -> {
                    String bookId = mSearchAdapter.getItem(pos).get_id();
                    BookDetailActivity.startActivity(this,bookId);
                }
        );
    }

    private void searchBook(){
        String query = mEtInput.getText().toString().trim();
        if(!query.equals("")){
            mRlRefresh.setVisibility(View.VISIBLE);
            mRlRefresh.showLoading();
            mPresenter.searchBook(query);
            //??????????????????
            mRlRefresh.showLoading();
            toggleKeyboard();
        }
    }

    private void toggleKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        //????????????
        mRlRefresh.setVisibility(View.GONE);
        //????????????
        mPresenter.searchHotWord();
    }

    @Override
    public void showError() {
    }

    @Override
    public void complete() {

    }

    @Override
    public void finishHotWords(List<String> hotWords) {
        mHotTagList = hotWords;
        refreshTag();
    }

    private void refreshTag(){
        int last = mTagStart + TAG_LIMIT;
        if (mHotTagList.size() <= last){
            mTagStart = 0;
            last = TAG_LIMIT;
        }
        List<String> tags = mHotTagList.subList(mTagStart, last);
        mTgHot.setTags(tags);
        mTagStart += TAG_LIMIT;
    }

    @Override
    public void finishKeyWords(List<String> keyWords) {
        if (keyWords.size() == 0) mRlRefresh.setVisibility(View.INVISIBLE);
        mKeyWordAdapter.refreshItems(keyWords);
        if (!(mRvSearch.getAdapter() instanceof KeyWordAdapter)){
            mRvSearch.setAdapter(mKeyWordAdapter);
        }
    }

    @Override
    public void finishBooks(List<SearchBookPackage.BooksBean> books) {
        mSearchAdapter.refreshItems(books);
        if (books.size() == 0){
            mRlRefresh.showEmpty();
        }
        else {
            //????????????
            mRlRefresh.showFinish();
        }
        //??????
        if (!(mRvSearch.getAdapter() instanceof SearchBookAdapter)){
            mRvSearch.setAdapter(mSearchAdapter);
        }
    }

    @Override
    public void errorBooks() {
        mRlRefresh.showEmpty();
    }

    @Override
    public void onBackPressed() {
        if (mRlRefresh.getVisibility() == View.VISIBLE){
            mEtInput.setText("");
        }
        else {
            super.onBackPressed();
        }
    }
}
