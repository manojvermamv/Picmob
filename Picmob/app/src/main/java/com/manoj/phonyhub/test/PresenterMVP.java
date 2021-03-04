package com.manoj.phonyhub.test;

public class PresenterMVP implements ContractMVP.Presenter, ContractMVP.Model.OnFinishedListener {

    // creating object of View Interface
    private ContractMVP.View mainView;

    // creating object of Model Interface
    private ContractMVP.Model model;

    // instantiating the objects of View and Model Interface
    public PresenterMVP(ContractMVP.View mainView, ContractMVP.Model model) {
        this.mainView = mainView;
        this.model = model;
    }

    @Override
    // operations to be performed on button click
    public void onButtonClick() {
        if (mainView != null) {
            mainView.showProgress();
        }
        model.getNextCourse(this);
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    // method to return the string
    // which will be displayed in the
    // Course Detail TextView
    public void onFinished(String string) {
        if (mainView != null) {
            mainView.setString(string);
            mainView.hideProgress();
        }
    }

}
