// Generated code from Butter Knife. Do not modify!
package cn.mstar.store.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RegisterActivity$$ViewBinder<T extends cn.mstar.store.activity.RegisterActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131559168, "field 'tv_actionbar_middle'");
    target.tv_actionbar_middle = finder.castView(view, 2131559168, "field 'tv_actionbar_middle'");
    view = finder.findRequiredView(source, 2131559173, "field 'tv_actionbar_right'");
    target.tv_actionbar_right = finder.castView(view, 2131559173, "field 'tv_actionbar_right'");
    view = finder.findRequiredView(source, 2131558600, "field 'tv_noaccount_register'");
    target.tv_noaccount_register = finder.castView(view, 2131558600, "field 'tv_noaccount_register'");
    view = finder.findRequiredView(source, 2131558701, "field 'tv_get_auth_code'");
    target.tv_get_auth_code = finder.castView(view, 2131558701, "field 'tv_get_auth_code'");
    view = finder.findRequiredView(source, 2131558695, "field 'ed_phonenumber'");
    target.ed_phonenumber = finder.castView(view, 2131558695, "field 'ed_phonenumber'");
    view = finder.findRequiredView(source, 2131558697, "field 'ed_password'");
    target.ed_password = finder.castView(view, 2131558697, "field 'ed_password'");
    view = finder.findRequiredView(source, 2131558700, "field 'ed_auth_number'");
    target.ed_auth_number = finder.castView(view, 2131558700, "field 'ed_auth_number'");
    view = finder.findRequiredView(source, 2131558709, "field 'iv_actionbar_left'");
    target.iv_actionbar_left = finder.castView(view, 2131558709, "field 'iv_actionbar_left'");
    view = finder.findRequiredView(source, 2131558530, "field 'iv_makepasswordvisible'");
    target.iv_makepasswordvisible = finder.castView(view, 2131558530, "field 'iv_makepasswordvisible'");
  }

  @Override public void unbind(T target) {
    target.tv_actionbar_middle = null;
    target.tv_actionbar_right = null;
    target.tv_noaccount_register = null;
    target.tv_get_auth_code = null;
    target.ed_phonenumber = null;
    target.ed_password = null;
    target.ed_auth_number = null;
    target.iv_actionbar_left = null;
    target.iv_makepasswordvisible = null;
  }
}
