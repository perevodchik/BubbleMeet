package com.perevodchik.bubblemeet.ui.mainmenu

import User
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.perevodchik.bubblemeet.util.Presenter
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.data.model.Profile
import com.perevodchik.bubblemeet.ui.filter.FilterActivity
import com.perevodchik.bubblemeet.ui.mainmenu.bubble.BubbleFragment
import com.perevodchik.bubblemeet.ui.mainmenu.fragment.*
import com.perevodchik.bubblemeet.ui.mainmenu.inbox.InboxFragment
import com.perevodchik.bubblemeet.ui.userprofile.UserProfileActivity
import com.perevodchik.bubblemeet.util.Api
import com.perevodchik.bubblemeet.util.Values
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MainPresenter(_ctx: MainActivity): View.OnClickListener,
    Presenter {
    private val context: MainActivity = _ctx
    private val api: Api = Api(context)
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var fm: FragmentManager
    private var matches: ImageView
    private var likes: ImageView
    private var menu: ImageView
    private var watchers: ImageView
    private var inbox: ImageView
    private var toggle: ImageView
    private var profileAvatar: CircleImageView
    private lateinit var fragment: Fragment

    init {
        fetchUser()
        fm = context.supportFragmentManager
        matches = context.findViewById(R.id.matches_item)
        likes = context.findViewById(R.id.likes_item)
        menu = context.findViewById(R.id.menu_item)
        watchers = context.findViewById(R.id.watchers_item)
        inbox = context.findViewById(R.id.inbox_item)
        toggle = context.findViewById(R.id.toggle)
        profileAvatar = context.findViewById(R.id.main_menu_user_avatar)

        matches.setOnClickListener(this)
        likes.setOnClickListener(this)
        watchers.setOnClickListener(this)
        menu.setOnClickListener(this)
        inbox.setOnClickListener(this)
        toggle.setOnClickListener(this)
        profileAvatar.setOnClickListener(this)
    }

    private fun fetchUser() {
        compositeDisposable
            .add(api.getUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<Response<Profile>>() {
            override fun onSuccess(r: Response<Profile>) {
                UserInstance.profile = r.body()
                val img = "${Values.imgUrl}/${UserInstance.profile?.avatarSmall}"
                Picasso.with(context).load(img).placeholder(R.drawable.background_loading).into(profileAvatar)
            }
            override fun onError(e: Throwable) {
                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }))
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.matches_item -> {
                fragment = MatchesFragment.newInstance()
                resetBottomMenuIcons()
                matches.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_matches_active
                    )
                )
                fm.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            R.id.likes_item -> {
                fragment = LikesFragment.newInstance()
                resetBottomMenuIcons()
                likes.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_likes_active
                    )
                )
                fm.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            R.id.menu_item -> {
                fragment = BubbleFragment.newInstance()
                resetBottomMenuIcons()
                fm.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit()

                toggle.visibility = View.VISIBLE
                toggle.setImageDrawable(context.getDrawable(R.drawable.filter_btn))
                toggle.setOnClickListener { context.startActivity(Intent(context, FilterActivity::class.java)) }
            }
            R.id.watchers_item -> {
                fragment = WatchersFragment.newInstance()
                resetBottomMenuIcons()
                watchers.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_watchers_active
                    )
                )
                fm.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            R.id.inbox_item -> {
                fragment = InboxFragment.newInstance()
                resetBottomMenuIcons()
                inbox.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_inbox_active
                    )
                )
                fm.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            R.id.toggle -> {
                context.startActivity(Intent(context, FilterActivity::class.java))
            }
            R.id.main_menu_user_avatar -> {
                context.startActivity(Intent(context, UserProfileActivity::class.java))
            }
        }
    }

    private fun resetBottomMenuIcons() {
        toggle.visibility = View.INVISIBLE

        context.findViewById<ImageView>(R.id.matches_item)?.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_matches_inactive
            )
        )
        context.findViewById<ImageView>(R.id.likes_item)?.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_likes_inactive
            )
        )
        context.findViewById<ImageView>(R.id.watchers_item)?.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_watchers_inactive
            )
        )
        context.findViewById<ImageView>(R.id.inbox_item)?.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.ic_inbox_inactive
            )
        )
    }

    override fun showMessage(t: Toast) {}

}