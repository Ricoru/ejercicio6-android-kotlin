package me.buddyoruna.app.map.contract

import me.buddyoruna.app.map.MainActivity

interface OnClick<T> {

    fun onClickItem(obj: T, position: Int, action: MainActivity.Companion.Actiones)

}