package br.com.douglasmotta.whitelabeltutorial

import android.view.View
import br.com.douglasmotta.whitelabeltutorial.config.Config
import javax.inject.Inject

class ConfigImpl @Inject constructor() : Config {

    override val addButtonVisibility: Int = View.VISIBLE
}