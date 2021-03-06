package com.novoda.movies.gallery

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.novoda.movies.R
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity(), GalleryPresenter.View {

    private val presenter = GalleryDependencyProvider().providerPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
    }

    override fun onStart() {
        super.onStart()
        presenter.startPresenting(this)
    }

    override fun onStop() {
        presenter.stopPresenting()
        super.onStop()
    }

    override fun renderError(message: String?) {
        label.text = message ?: getString(R.string.gallery_error)
    }

    override fun render(gallery: Gallery) {
        label.text = resources.getString(R.string.gallery_items, gallery.moviePosters.size)
    }
}
