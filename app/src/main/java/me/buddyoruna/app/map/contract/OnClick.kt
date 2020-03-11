package me.buddyoruna.app.map.contract

interface OnClick<T> {

    fun onClickItem(obj: T, position: Int)

}