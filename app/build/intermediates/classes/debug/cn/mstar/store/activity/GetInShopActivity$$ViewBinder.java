// Generated code from Butter Knife. Do not modify!
package cn.mstar.store.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GetInShopActivity$$ViewBinder<T extends cn.mstar.store.activity.GetInShopActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558709, "field 'back' and method 'back'");
    target.back = finder.castView(view, 2131558709, "field 'back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.back();
        }
      });
    view = finder.findRequiredView(source, 2131559168, "field 'title'");
    target.title = finder.castView(view, 2131559168, "field 'title'");
    view = finder.findRequiredView(source, 2131558896, "field 'address' and method 'selectAddress'");
    target.address = finder.castView(view, 2131558896, "field 'address'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.selectAddress(p0);
        }
      });
    view = finder.findRequiredView(source, 2131558897, "field 'shopList'");
    target.shopList = finder.castView(view, 2131558897, "field 'shopList'");
  }

  @Override public void unbind(T target) {
    target.back = null;
    target.title = null;
    target.address = null;
    target.shopList = null;
  }
}
