package com.hqbb.duanzi.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqbb.duanzi.R;
import com.hqbb.duanzi.adapter.TopRankAdapter;
import com.hqbb.duanzi.base.BaseBookActivity;
import com.hqbb.duanzi.base.OnRvItemClickListener;
import com.hqbb.duanzi.bean.RankingList;
import com.hqbb.duanzi.component.AppComponent;
import com.hqbb.duanzi.component.DaggerFindComponent;
import com.hqbb.duanzi.contract.TopRankContract;
import com.hqbb.duanzi.presenter.TopRankPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class TopRankActivity extends BaseBookActivity implements TopRankContract.View {


    @BindView(R.id.elvFeMale)
    ExpandableListView elvFeMale;
    @BindView(R.id.elvMale)
    ExpandableListView elvMale;

    private List<RankingList.MaleBean> maleGroups = new ArrayList<>();
    private List<List<RankingList.MaleBean>> maleChilds = new ArrayList<>();
    private TopRankAdapter maleAdapter;

    private List<RankingList.MaleBean> femaleGroups = new ArrayList<>();
    private List<List<RankingList.MaleBean>> femaleChilds = new ArrayList<>();
    private TopRankAdapter femaleAdapter;

    @Inject
    TopRankPresenter mPresenter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, TopRankActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_top_rank;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerFindComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void initToolBar() {
        mCommonToolbar.setTitle("排行榜");
        mCommonToolbar.setNavigationIcon(R.mipmap.ab_back);

    }

    @Override
    public void initDatas() {
        maleAdapter = new TopRankAdapter(this, maleGroups, maleChilds);
        femaleAdapter = new TopRankAdapter(this, femaleGroups, femaleChilds);
        maleAdapter.setItemClickListener(new ClickListener());
        femaleAdapter.setItemClickListener(new ClickListener());
    }

    @Override
    public void configViews() {
        showDialog();
        elvMale.setAdapter(maleAdapter);
        elvFeMale.setAdapter(femaleAdapter);

        mPresenter.attachView(this);
        mPresenter.getRankList();
    }

    @Override
    public void showRankList(RankingList rankingList) {
        maleGroups.clear();
        femaleGroups.clear();
        updateMale(rankingList);
        updateFemale(rankingList);
    }

    private void updateMale(RankingList rankingList) {
        List<RankingList.MaleBean> list = rankingList.male;
        List<RankingList.MaleBean> collapse = new ArrayList<>();
        for (RankingList.MaleBean bean : list) {
            if (bean.collapse) { // 折叠
                collapse.add(bean);
            } else {
                maleGroups.add(bean);
                maleChilds.add(new ArrayList<RankingList.MaleBean>());
            }
        }
        if (collapse.size() > 0) {
            maleGroups.add(new RankingList.MaleBean("别人家的排行榜"));
            maleChilds.add(collapse);
        }
        maleAdapter.notifyDataSetChanged();
    }

    private void updateFemale(RankingList rankingList) {
        List<RankingList.MaleBean> list = rankingList.female;
        List<RankingList.MaleBean> collapse = new ArrayList<>();
        for (RankingList.MaleBean bean : list) {
            if (bean.collapse) { // 折叠
                collapse.add(bean);
            } else {
                femaleGroups.add(bean);
                femaleChilds.add(new ArrayList<RankingList.MaleBean>());
            }
        }
        if (collapse.size() > 0) {
            femaleGroups.add(new RankingList.MaleBean("别人家的排行榜"));
            femaleChilds.add(collapse);
        }
        femaleAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {
        dismissDialog();
    }

    class ClickListener implements OnRvItemClickListener<RankingList.MaleBean> {

        @Override
        public void onItemClick(View view, int position, RankingList.MaleBean data) {
            if (data.monthRank == null) {
                SubOtherHomeRankActivity.startActivity(mContext, data._id, data.title);
            } else {
                SubRankActivity.startActivity(mContext, data._id, data.monthRank, data.totalRank, data.title);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
