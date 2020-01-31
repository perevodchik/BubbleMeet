package com.perevodchik.bubblemeet.ui.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.perevodchik.bubblemeet.R
import com.perevodchik.bubblemeet.data.adapter.TemporaryAdapter
import com.perevodchik.bubblemeet.data.model.UserData
import com.perevodchik.bubblemeet.ui.filter.FilterActivity
import com.perevodchik.bubblemeet.ui.mainmenu.MainActivity
import com.perevodchik.bubblemeet.ui.mainmenu.inbox.ChatFragment
import com.perevodchik.bubblemeet.ui.mainmenu.inbox.InboxFragment
import com.perevodchik.bubblemeet.ui.mainmenu.presenter.LikesPresenter
import com.perevodchik.bubblemeet.util.OnSwipeListener
import com.perevodchik.bubblemeet.util.UserInstance
import com.perevodchik.bubblemeet.util.Values
import com.sackcentury.shinebuttonlib.ShineButton
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserPreviewFragment(_userData: UserData, _fm: FragmentManager) : Fragment() {
    private var userData: UserData = _userData
    private val presenter = UserPreviewPresenter(this)
    private val fm: FragmentManager = _fm
    private lateinit var userImg: CircleImageView
    private lateinit var avatarBackground: ImageView
    private lateinit var likeBtn: ImageView
    private lateinit var sendMailBtn: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var oldCountryTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TemporaryAdapter
    private lateinit var shineButton: ShineButton
    private lateinit var toggleBtn: ImageView
    private var isUserFavorite = false
    private var flag: Int = 1

    companion object {
        fun newInstance(_userData: UserData, _fm: FragmentManager) = UserPreviewFragment(_userData, _fm)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userImg = view.findViewById(R.id.user_profile_avatar)
        avatarBackground = view.findViewById(R.id.user_preview_avatar_background)
        likeBtn = view.findViewById(R.id.like_button)
        sendMailBtn = view.findViewById(R.id.message_button)
        nameTextView = view.findViewById(R.id.preview_user_name)
        oldCountryTextView = view.findViewById(R.id.preview_year_city)
        recyclerView = view.findViewById(R.id.profile_preview_list)
        shineButton = view.findViewById(R.id.shine_button)
        toggleBtn = activity!!.findViewById(R.id.toggle)

        toggleBtn.visibility = View.VISIBLE
        toggleBtn.setImageDrawable(activity!!.resources.getDrawable(R.drawable.ic_back, null))
        toggleBtn.setOnClickListener {
            activity!!.findViewById<ImageView>(R.id.toggle).visibility = View.INVISIBLE
            activity!!.supportFragmentManager.popBackStack()
        }

        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        adapter = TemporaryAdapter(
            _ctx = activity as MainActivity?,
            _fm = fragmentManager,
            _list = mutableListOf()
        )

        presenter.getTemporary()
        recyclerView.adapter = adapter

        init(userData)

        val animationAlphaBubbles = AnimationUtils.loadAnimation(context, R.anim.alpha_profile)
        animationAlphaBubbles.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                val animationAlphaOtherViews =
                    AnimationUtils.loadAnimation(context, R.anim.alpha_profile)

                nameTextView.animation = animationAlphaOtherViews
                oldCountryTextView.animation = animationAlphaOtherViews
                likeBtn.animation = animationAlphaOtherViews
                sendMailBtn.animation = animationAlphaOtherViews
                userImg.animation = animationAlphaOtherViews
                recyclerView.animation = animationAlphaOtherViews
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        avatarBackground.animation = animationAlphaBubbles
        animationAlphaBubbles.start()
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    private fun init(u: UserData) {
        userData = u
        nameTextView.text = userData.name
        oldCountryTextView.text = "${userData.age}, ${userData.city}"

        isUserFavorite = presenter.isUserInFavorite(userData)
        if(isUserFavorite)
            likeBtn.setImageDrawable(resources.getDrawable(R.drawable.likes_button_boom, null))
        else
            likeBtn.setImageDrawable(resources.getDrawable(R.drawable.likes_button_profile, null))

        val anim = AnimationUtils.loadAnimation(context, R.anim.scale_like)
        val animBack = AnimationUtils.loadAnimation(context, R.anim.scale_like_back)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                likeBtn.startAnimation(animBack)
            }

            override fun onAnimationStart(animation: Animation?) {}
        })

        likeBtn.setOnClickListener {
            isUserFavorite = !isUserFavorite
            if(isUserFavorite)
                likeBtn.setImageDrawable(resources.getDrawable(R.drawable.likes_button_boom, null))
            else
                likeBtn.setImageDrawable(resources.getDrawable(R.drawable.likes_button_profile, null))
            flag++

            if(isUserFavorite)
                likeBtn.startAnimation(anim)
        }

        sendMailBtn.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container, ChatFragment(userData.id))
                .addToBackStack(null)
                .commit()
        }

        userImg.setOnTouchListener(object : OnSwipeListener(context!!) {
            override fun onSwipeTop() {
                super.onSwipeTop()
                activity?.supportFragmentManager?.popBackStack()
            }

            override fun onSwipeBottom() {
                presenter.addTemporary(userData.id)
                adapter.addUserToStart(userData)
                val anim0 = AnimationUtils.loadAnimation(context, R.anim.swipe_bottom)
                anim0.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {}

                    override fun onAnimationEnd(animation: Animation) {
                        userImg.animate().translationY(1.0f)
                            .translationY(-1.0f)
                        userImg.animation = AnimationUtils.loadAnimation(
                            context,
                            R.anim.alpha_profile
                        )
                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                userImg.startAnimation(anim0)
            }

            override fun onSwipeLeft() {
                boomAnimation(false)
            }

            override fun onSwipeRight() {
                boomAnimation(true)
            }

            override fun onClick() {
                super.onClick()
                presenter.openFullProfileIfCan(userData, fm)
            }
        })

        try {
            Picasso.with(view?.context)
                .load("${Values.imgUrl}/${userData.avatarFull}")
                .into(userImg)
        } catch (e: Exception) {
            println(e.localizedMessage)
        }
    }

    override fun onStop() {
        super.onStop()
        if(flag % 2 == 1)
            return

        when(isUserFavorite) {
            true -> {
                presenter.addFavorite(userData)
            }
            false -> {
                presenter.deleteFavorite(userData)
                UserInstance.removeLikes(userData)
            }
        }
    }

    fun boomAnimation(isRight: Boolean) {
        this.shineButton.performClick()
        this.shineButton.isChecked = false
        if (isRight) {
            Handler().postDelayed({
                val transaction = fm.beginTransaction()
                val profileFragment = UserFullFragment(userData)
                transaction.replace(R.id.container, profileFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }, 750)
        } else {
            Handler().postDelayed({
                for(c in 0 until UserInstance.allUsers.size) {
                    val ud = UserInstance.allUsers[c]
                    if(ud.id == userData.id)
                        if(c + 1 < UserInstance.allUsers.size) {
                            init(UserInstance.allUsers[c + 1])
                            break
                        }
                }
            }, 750)
        }
    }

    fun setUsers(list: List<UserData>) {
        adapter.setUsers(list)
    }
}