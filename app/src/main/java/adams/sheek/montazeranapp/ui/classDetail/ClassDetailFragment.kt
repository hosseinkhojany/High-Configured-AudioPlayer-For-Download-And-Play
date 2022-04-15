package adams.sheek.montazeranapp.ui.classDetail

import adams.sheek.montazeranapp.data.datasource.ram.InRamDs
import adams.sheek.montazeranapp.data.model.Topic
import adams.sheek.montazeranapp.databinding.FragmentClassDetailBinding
import adams.sheek.montazeranapp.ui.shared.ClassesViewModel
import adams.sheek.montazeranapp.utils.exo.DemoUtil
import adams.sheek.montazeranapp.utils.exo.PlayerForegroundService
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaItem.fromUri
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.offline.DownloadRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClassDetailFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentClassDetailBinding
    private val viewModel: ClassesViewModel by activityViewModels()

    @Inject
    lateinit var player: ExoPlayer
    private var mediaItem: MediaItem? = null
    private var startAutoPlay = false
    private var startItemIndex = 0
    private var startPosition: Long = 0
    private lateinit var topic: Topic

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        clearStartPosition()
        binding = FragmentClassDetailBinding.inflate(inflater)

        binding.playerControllerView.setShowPreviousButton(false)
        binding.playerControllerView.setShowNextButton(false)
        binding.playerControllerView.setShowShuffleButton(true)
        binding.playerControllerView.controllerHideOnTouch = false
        binding.playerControllerView.controllerShowTimeoutMs = 1000000000
        binding.playerControllerView.showController()

        topic = InRamDs.listTopics[viewModel.selectedTopic]

        binding.header.text = topic.name
        binding.classTitle.text = viewModel.selectedTopicFile!!.title!!
        binding.allTime.text = viewModel.selectedTopicFile!!.duration!!
        Glide.with(requireContext()).load(InRamDs.listTopics[InRamDs.selectedTopicPositionInList].image).into(binding.imageView3)

        if (viewModel.selectedTopicFile?.downloadUrl?.isNotEmpty() == true){
            initPlayer(viewModel.selectedTopicFile?.downloadUrl!!)
        }

        setOnClickListeners()
        return binding.root
    }

    private fun setOnClickListeners(){
        binding.loop.setOnClickListener(this)
        binding.backTrack.setOnClickListener(this)
        binding.play.setOnClickListener(this)
        binding.nextTrack.setOnClickListener(this)
        binding.reset.setOnClickListener(this)
        binding.back.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v){
            binding.loop -> {}
            binding.backTrack -> {}
            binding.play -> {}
            binding.nextTrack -> {}
            binding.reset -> {}
            binding.back -> {findNavController().popBackStack()}
        }
    }

    private fun initPlayer(url: String) {
        if (initializePlayer(url)){
            player.seekTo(viewModel.selectedTopicFile!!.elapsedPosition.toLong())
            if (!InRamDs.playerServiceIsRunning){
                val intent = Intent(requireContext(), PlayerForegroundService::class.java)
                intent.putExtra("fromApp", "true")
                requireActivity().startService(intent)
            }
        }
    }
    private fun initializePlayer(url: String): Boolean {
        mediaItem = createMediaItem(url)
        if (mediaItem == null) {
            return false
        }
        binding.playerControllerView.player = player
        player.setMediaItem(mediaItem!!)
        player.prepare()
        return true
    }

    private fun clearPlayer() {
        updateStartPosition()
//        player.release()
        player.clearMediaItems()
        mediaItem = null
    }

    private fun updateStartPosition() {
        startAutoPlay = player.playWhenReady
        startItemIndex = player.currentMediaItemIndex
        startPosition = Math.max(0, player.contentPosition)
    }

    private fun clearStartPosition() {
        startAutoPlay = true
        startItemIndex = C.INDEX_UNSET
        startPosition = C.TIME_UNSET
    }
    private fun createMediaItem(
        url: String
    ): MediaItem {
        val downloadRequest: DownloadRequest? =
            DemoUtil.getDownloadTracker( /* context= */requireContext()).getDownloadRequest(Uri.parse(url))
        val builder = fromUri(Uri.parse(url)).buildUpon()
            builder.setMediaMetadata(MediaMetadata.Builder().setDescription(topic.name+")-("+viewModel.selectedTopicFile!!.title+")-("+viewModel.selectedTopicFile!!.filename).build())
        return if (downloadRequest != null) {
            builder
                .setMediaId(downloadRequest.id)
                .setUri(downloadRequest.uri)
                .setCustomCacheKey(downloadRequest.customCacheKey)
                .setMimeType(downloadRequest.mimeType)
                .setStreamKeys(downloadRequest.streamKeys)
            builder.build()
        } else {
            builder.build()
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveCurrentMediaState(viewModel.selectedTopicFile?.filename!!, player.currentPosition.toFloat())
    }

}