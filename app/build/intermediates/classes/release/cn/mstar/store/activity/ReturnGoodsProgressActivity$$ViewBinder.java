// Generated code from Butter Knife. Do not modify!
package cn.mstar.store.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ReturnGoodsProgressActivity$$ViewBinder<T extends cn.mstar.store.activity.ReturnGoodsProgressActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131559120, "field 'rel_progress_1'");
    target.rel_progress_1 = finder.castView(view, 2131559120, "field 'rel_progress_1'");
    view = finder.findRequiredView(source, 2131559121, "field 'rel_progress_2'");
    target.rel_progress_2 = finder.castView(view, 2131559121, "field 'rel_progress_2'");
    view = finder.findRequiredView(source, 2131559122, "field 'rel_progress_3'");
    target.rel_progress_3 = finder.castView(view, 2131559122, "field 'rel_progress_3'");
    view = finder.findRequiredView(source, 2131559123, "field 'iv_icon_progress_1'");
    target.iv_icon_progress_1 = finder.castView(view, 2131559123, "field 'iv_icon_progress_1'");
    view = finder.findRequiredView(source, 2131559124, "field 'iv_icon_progress_2'");
    target.iv_icon_progress_2 = finder.castView(view, 2131559124, "field 'iv_icon_progress_2'");
    view = finder.findRequiredView(source, 2131559125, "field 'iv_icon_progress_3'");
    target.iv_icon_progress_3 = finder.castView(view, 2131559125, "field 'iv_icon_progress_3'");
    view = finder.findRequiredView(source, 2131559126, "field 'iv_icon_progress_4'");
    target.iv_icon_progress_4 = finder.castView(view, 2131559126, "field 'iv_icon_progress_4'");
    view = finder.findRequiredView(source, 2131558705, "field 'tv_order_id'");
    target.tv_order_id = finder.castView(view, 2131558705, "field 'tv_order_id'");
    view = finder.findRequiredView(source, 2131558704, "field 'tv_date'");
    target.tv_date = finder.castView(view, 2131558704, "field 'tv_date'");
    view = finder.findRequiredView(source, 2131558706, "field 'lny_logistic_details'");
    target.lny_logistic_details = finder.castView(view, 2131558706, "field 'lny_logistic_details'");
    view = finder.findRequiredView(source, 2131558707, "field 'lny_about_product'");
    target.lny_about_product = finder.castView(view, 2131558707, "field 'lny_about_product'");
    view = finder.findRequiredView(source, 2131558708, "field 'lny_problem_description'");
    target.lny_problem_description = finder.castView(view, 2131558708, "field 'lny_problem_description'");
    view = finder.findRequiredView(source, 2131558709, "field 'iv_back' and method 'back'");
    target.iv_back = finder.castView(view, 2131558709, "field 'iv_back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.back();
        }
      });
    view = finder.findRequiredView(source, 2131559168, "field 'tv_title'");
    target.tv_title = finder.castView(view, 2131559168, "field 'tv_title'");
  }

  @Override public void unbind(T target) {
    target.rel_progress_1 = null;
    target.rel_progress_2 = null;
    target.rel_progress_3 = null;
    target.iv_icon_progress_1 = null;
    target.iv_icon_progress_2 = null;
    target.iv_icon_progress_3 = null;
    target.iv_icon_progress_4 = null;
    target.tv_order_id = null;
    target.tv_date = null;
    target.lny_logistic_details = null;
    target.lny_about_product = null;
    target.lny_problem_description = null;
    target.iv_back = null;
    target.tv_title = null;
  }
}
