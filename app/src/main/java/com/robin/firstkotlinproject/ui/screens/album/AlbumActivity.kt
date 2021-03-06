package com.robin.firstkotlinproject.ui.screens.album

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.WindowManager
import com.robin.firstkotlinproject.R
import com.robin.firstkotlinproject.ui.di.ApplicationComponent
import com.robin.firstkotlinproject.ui.di.subcomponent.album.AlbumActivityModule
import com.robin.firstkotlinproject.ui.activity.BaseActivity
import com.robin.firstkotlinproject.ui.adapter.TracksAdapter
import com.robin.firstkotlinproject.ui.model.AlbumDetail
import com.robin.firstkotlinproject.ui.model.TrackDetail
import com.robin.firstkotlinproject.ui.model.mapper.TrackDataMapper
import com.robin.firstkotlinproject.ui.presenter.AlbumPresenter
import com.robin.firstkotlinproject.ui.util.getNavigationId
import com.robin.firstkotlinproject.ui.util.supportsLollipop
import com.robin.firstkotlinproject.ui.view.AlbumView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.jetbrains.anko.dimen
import javax.inject.Inject

/**
 * Created by Robin Yang on 11/21/17.
 */
class AlbumActivity : BaseActivity<AlbumLayout>(), AlbumView {

    override val ui = AlbumLayout()

    companion object {
        private val listAnimationStartDelay = 500L
        private val noTranslation = 0f
        private val transparent = 0f
    }

    val albumListBreakingEdgeHeight by lazy { dimen(R.dimen.album_breaking_edge_height).toFloat() }

    @Inject
    @VisibleForTesting
    lateinit var presenter: AlbumPresenter

    @Inject
    lateinit var trackDataMapper: TrackDataMapper

    @Inject
    lateinit var adapter: TracksAdapter

    @Inject
    lateinit var layoutManager: LinearLayoutManager

    @Inject
    lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpTransition()
        setUpActionBar()
        setUpTrackList()
    }

    override fun injectDependencies(applicationComponent: ApplicationComponent) {
        applicationComponent.plus(AlbumActivityModule(this))
                .injectTo(this)
    }

    @SuppressLint("NewApi")
    private fun setUpTransition() {
        supportPostponeEnterTransition()
        supportsLollipop { ui.image.transitionName = IMAGE_TRANSITION_NAME }
    }

    private fun setUpTrackList() {
        ui.trackList.adapter = adapter
        ui.trackList.layoutManager = layoutManager
        ui.listCard.translationY = -albumListBreakingEdgeHeight
    }

    private fun setUpActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = null
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
        presenter.init(getNavigationId())
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun showAlbum(albumDetail: AlbumDetail?) {
        if (albumDetail != null) {
            picasso.load(albumDetail.url)
                    .fit()
                    .centerCrop()
                    .into(ui.image, object : Callback.EmptyCallback() {
                        override fun onSuccess() {
                            makeStatusBarTransparent()
                            supportStartPostponedEnterTransition()
                            populateTrackList(trackDataMapper.transform(albumDetail.tracks))
                            animateTrackListUp()
                        }
                    })
        } else {
            supportStartPostponedEnterTransition()
            supportFinishAfterTransition()
        }
    }

    private fun animateTrackListUp() {
        ui.listCard.animate().setStartDelay(listAnimationStartDelay).translationY(noTranslation)
    }

    private fun populateTrackList(trackDetails: List<TrackDetail>) {
        adapter.items = trackDetails
    }

    @SuppressLint("InlinedApi")
    private fun makeStatusBarTransparent() {
        supportsLollipop {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null && item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        ui.listCard.animate().alpha(transparent).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                supportFinishAfterTransition()
            }
        })
    }
}