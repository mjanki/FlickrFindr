package org.br.flickrfinder.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.br.flickrfinder.R
import org.br.flickrfinder.models.ErrorNetworkViewEntity
import org.br.util.setupToolbar

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupToolbar(toolbar, showUp = false, title = getString(R.string.app_name))
    }

    override fun handleErrorNetwork(errorNetworkViewEntity: ErrorNetworkViewEntity) {
        Snackbar.make(
                clContainer,
                "${errorNetworkViewEntity.action} failed with ${errorNetworkViewEntity.code}",
                Snackbar.LENGTH_LONG
        ).addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                resolveErrorNetwork()
            }
        }).show()
    }
}