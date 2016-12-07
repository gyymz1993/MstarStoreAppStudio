// Generated code from Butter Knife. Do not modify!
package cn.mstar.store.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GoodsManagement_InnerAdapter$LinearItemViewHolder$$ViewBinder<T extends cn.mstar.store.adapter.GoodsManagement_InnerAdapter.LinearItemViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558547, "field 'productIV'");
    target.productIV = finder.castView(view, 2131558547, "field 'productIV'");
    view = finder.findRequiredView(source, 2131558548, "field 'productName'");
    target.productName = finder.castView(view, 2131558548, "field 'productName'");
    view = finder.findRequiredView(source, 2131558549, "field 'productNorms'");
    target.productNorms = finder.castView(view, 2131558549, "field 'productNorms'");
    view = finder.findRequiredView(source, 2131558550, "field 'productPrice'");
    target.productPrice = finder.castView(view, 2131558550, "field 'productPrice'");
    view = finder.findRequiredView(source, 2131558551, "field 'productNums'");
    target.productNums = finder.castView(view, 2131558551, "field 'productNums'");
    view = finder.findRequiredView(source, 2131558938, "field 'tv_return_request_progress'");
    target.tv_return_request_progress = finder.castView(view, 2131558938, "field 'tv_return_request_progress'");
    view = finder.findRequiredView(source, 2131558940, "field 'rel_bottom'");
    target.rel_bottom = view;
    view = finder.findRequiredView(source, 2131558939, "field 'go_to_comment'");
    target.go_to_comment = finder.castView(view, 2131558939, "field 'go_to_comment'");
  }

  @Override public void unbind(T target) {
    target.productIV = null;
    target.productName = null;
    target.productNorms = null;
    target.productPrice = null;
    target.productNums = null;
    target.tv_return_request_progress = null;
    target.rel_bottom = null;
    target.go_to_comment = null;
  }
}
