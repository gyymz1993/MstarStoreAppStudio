// Generated code from Butter Knife. Do not modify!
package cn.mstar.store.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PostCommentActivity$$ViewBinder<T extends cn.mstar.store.activity.PostCommentActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558640, "field 'radioGroup'");
    target.radioGroup = finder.castView(view, 2131558640, "field 'radioGroup'");
    view = finder.findRequiredView(source, 2131558638, "field 'iv_product_img'");
    target.iv_product_img = finder.castView(view, 2131558638, "field 'iv_product_img'");
    view = finder.findRequiredView(source, 2131558548, "field 'tv_product_name'");
    target.tv_product_name = finder.castView(view, 2131558548, "field 'tv_product_name'");
    view = finder.findRequiredView(source, 2131558549, "field 'tv_product_norms'");
    target.tv_product_norms = finder.castView(view, 2131558549, "field 'tv_product_norms'");
    view = finder.findRequiredView(source, 2131558645, "field 'iv_btn_camera'");
    target.iv_btn_camera = finder.castView(view, 2131558645, "field 'iv_btn_camera'");
    view = finder.findRequiredView(source, 2131558644, "field 'edittext'");
    target.edittext = finder.castView(view, 2131558644, "field 'edittext'");
    view = finder.findRequiredView(source, 2131558646, "field 'comment_gridView'");
    target.comment_gridView = finder.castView(view, 2131558646, "field 'comment_gridView'");
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
    view = finder.findRequiredView(source, 2131558654, "method 'sendComment'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendComment();
        }
      });
    target.rb = Finder.listOf(
        finder.<android.widget.RadioButton>findRequiredView(source, 2131558641, "field 'rb'"),
        finder.<android.widget.RadioButton>findRequiredView(source, 2131558642, "field 'rb'"),
        finder.<android.widget.RadioButton>findRequiredView(source, 2131558643, "field 'rb'")
    );
  }

  @Override public void unbind(T target) {
    target.radioGroup = null;
    target.iv_product_img = null;
    target.tv_product_name = null;
    target.tv_product_norms = null;
    target.iv_btn_camera = null;
    target.edittext = null;
    target.comment_gridView = null;
    target.iv_back = null;
    target.tv_title = null;
    target.rb = null;
  }
}
