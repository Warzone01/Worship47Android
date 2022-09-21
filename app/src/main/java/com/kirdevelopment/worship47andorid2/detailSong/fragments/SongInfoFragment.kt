package com.kirdevelopment.worship47andorid2.detailSong.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kirdevelopment.worship47andorid2.ClickListeners
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.FragmentSongInfoBinding
import com.kirdevelopment.worship47andorid2.utils.Constants
import com.kirdevelopment.worship47andorid2.utils.StringBuilder

class SongInfoFragment : Fragment(), ClickListeners {

    private lateinit var binding: FragmentSongInfoBinding

    private var author: String? = null
    private var presentation: String? = null
    private var chords: ArrayList<String>? = null
    private var videos: ArrayList<String>? = null
    private var category: ArrayList<String>? = null
    private var translator: ArrayList<String>? = null
    private var keys: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            getArgs(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongInfoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setData()

        binding.root.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.ivClose.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    // получает нужные данные о песне
    private fun getArgs(bundle: Bundle) {
        author = bundle.getString(Constants.AUTHOR)
        presentation = bundle.getString(Constants.PRESENTATION)
        chords = bundle.getStringArrayList(Constants.CHORDS)
        translator = bundle.getStringArrayList(Constants.TRANSLATOR)
        category = bundle.getStringArrayList(Constants.CATEGORY)
        videos = bundle.getStringArrayList(Constants.VIDEOS)
        keys = bundle.getStringArrayList(Constants.KEYS)
    }

    // устанавливает информацию
    private fun setData() {
        binding.apply {
            if (!category.isNullOrEmpty()) {
                llCategoryContainer.visibility = View.VISIBLE
                tvCategory.text = getString(
                    R.string.category,
                    StringBuilder.buildFromListToString(category ?: ArrayList())
                )
            }
            if (!author.isNullOrBlank()) {
                llAuthorContainer.visibility = View.VISIBLE
                tvAuthor.text = getString(R.string.author, author)
            }
            if (!translator.isNullOrEmpty()) {
                if (translator!![0].isNotBlank())llTranslatorContainer.visibility = View.VISIBLE
                tvTranslator.text = getString(
                    R.string.translator,
                    StringBuilder.buildFromListToString(translator ?: ArrayList())
                )
            }
            if (!chords.isNullOrEmpty()) {
                if(chords!![0].isNotBlank()) llChordsContainer.visibility = View.VISIBLE
                binding.chordDownloadButtons.apply {
                    setButtons(chords ?: ArrayList(), keys ?: ArrayList(), this@SongInfoFragment)
                }

            }
            if (!presentation.isNullOrEmpty()) {
                llPresentationContainer.visibility = View.VISIBLE
                tvPresentation.setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(presentation ?: "")))
                }
            }
            if (!videos.isNullOrEmpty()) {
                if(videos!![0].isNotBlank())  llVideosContainer.visibility = View.VISIBLE
                videosList.setVideo(videos?: ArrayList(),this@SongInfoFragment)

            }
        }
    }

    // получает информацию о песне
    companion object {
        fun newInstance(
            category: ArrayList<String>?,
            author: String?,
            translator: ArrayList<String>?,
            chords: ArrayList<String>?,
            keys: ArrayList<String>,
            presentation: String?,
            videos: ArrayList<String>?
        ): SongInfoFragment {
            val args = Bundle()
            args.putString(Constants.AUTHOR, author)
            args.putString(Constants.PRESENTATION, presentation)
            args.putStringArrayList(Constants.CATEGORY, category)
            args.putStringArrayList(Constants.TRANSLATOR, translator)
            args.putStringArrayList(Constants.CHORDS, chords)
            args.putStringArrayList(Constants.VIDEOS, videos)
            args.putStringArrayList(Constants.KEYS, keys)
            val fragment = SongInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun downloadChordClickListener(buttonIndex: Int) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(chords?.get(buttonIndex) ?: "")))
    }

    override fun videoPreviewClickListener(videoIndex: Int) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(Constants.LINK_BUILDER + videos?.get(videoIndex))
            )
        )
    }
}