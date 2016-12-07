// Generated code from Butter Knife. Do not modify!
package cn.mstar.store.fragments;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MySelfFragment$$ViewBinder<T extends cn.mstar.store.fragments.MySelfFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558874, "field 'tv_Integral'");
    target.tv_Integral = finder.castView(view, 2131558874, "field 'tv_Integral'");
    view = finder.findRequiredView(source, 2131558721, "field 'scrollView'");
    target.scrollView = finder.castView(view, 2131558721, "field 'scrollView'");
    view = finder.findRequiredView(source, 2131559168, "field 'titlebar_title'");
    target.titlebar_title = finder.castView(view, 2131559168, "field 'titlebar_title'");
    view = finder.findRequiredView(source, 2131559171, "field 'titlebar_message'");
    target.titlebar_message = finder.castView(view, 2131559171, "field 'titlebar_message'");
    view = finder.findRequiredView(source, 2131558871, "field 'iv_head'");
    target.iv_head = finder.castView(view, 2131558871, "field 'iv_head'");
  }

  @Override public void unbind(T target) {
    target.tv_Integral = null;
    target.scrollView = null;
    target.titlebar_title = null;
    target.titlebar_message = null;
    target.iv_head = null;
  }
}
