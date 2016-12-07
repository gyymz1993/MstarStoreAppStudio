// Generated code from Butter Knife. Do not modify!
package cn.mstar.store.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends cn.mstar.store.activity.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131558412, "field 'radioHome'");
    target.radioHome = finder.castView(view, 2131558412, "field 'radioHome'");
    view = finder.findRequiredView(source, 2131559160, "field 'radioClassification'");
    target.radioClassification = finder.castView(view, 2131559160, "field 'radioClassification'");
    view = finder.findRequiredView(source, 2131559161, "field 'radioShoppingCart'");
    target.radioShoppingCart = finder.castView(view, 2131559161, "field 'radioShoppingCart'");
    view = finder.findRequiredView(source, 2131559163, "field 'radioMe'");
    target.radioMe = finder.castView(view, 2131559163, "field 'radioMe'");
  }

  @Override public void unbind(T target) {
    target.radioHome = null;
    target.radioClassification = null;
    target.radioShoppingCart = null;
    target.radioMe = null;
  }
}
