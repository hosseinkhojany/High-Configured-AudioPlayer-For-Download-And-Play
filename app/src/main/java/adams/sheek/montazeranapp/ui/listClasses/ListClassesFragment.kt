package adams.sheek.montazeranapp.ui.listClasses

import adams.sheek.montazeranapp.data.datasource.ram.InRamDs
import adams.sheek.montazeranapp.data.repositories.TopicRepository
import adams.sheek.montazeranapp.databinding.FragmentListClassesBinding
import adams.sheek.montazeranapp.ui.adapter.ListClassesAdapter
import adams.sheek.montazeranapp.ui.shared.ClassesViewModel
import adams.sheek.montazeranapp.utils.exo.DemoUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import adams.sheek.montazeranapp.utils.animatedThemeManager.ThemeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListClassesFragment : Fragment() {

    private lateinit var binding: FragmentListClassesBinding
    private val viewModel: ClassesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListClassesBinding.inflate(inflater)

        if (viewModel.selectedTopic > -1){
            val topic = InRamDs.listTopics[viewModel.selectedTopic]
            binding.listClasses.adapter = ListClassesAdapter(topic = topic,
                onBackClick =  {
                    findNavController().popBackStack()
                }, onItemClick = { it, position ->
                    viewModel.selectedTopicFile = it
                    if (it.downloadUrl?.isNotEmpty() == true){
                        findNavController().navigate(ListClassesFragmentDirections.actionListClassesFragmentToClassDetailFragment())
                    }else{
                        viewModel.downloadFile(it){
                            binding.listClasses.adapter?.notifyItemChanged(position)
                            DemoUtil.getDownloadTracker(requireContext()).toggleDownload(it)
                            findNavController().navigate(ListClassesFragmentDirections.actionListClassesFragmentToClassDetailFragment())
                        }
                    }
                },onDownloadClick =  { topicFile, position ->
                    if (topicFile.downloadUrl?.isNotEmpty() == true){
                        binding.listClasses.adapter?.notifyItemChanged(position)
                        DemoUtil.getDownloadTracker(requireContext()).toggleDownload(topicFile.downloadUrl!!)
                    }else{
                        viewModel.downloadFile(topicFile){
                            binding.listClasses.adapter?.notifyItemChanged(position)
                            DemoUtil.getDownloadTracker(requireContext()).toggleDownload(it)
                        }
                    }
                })
        }

        viewModel.registerOnDownloadChangeReceiver { changed, item ->
            if (changed){
                binding.listClasses.adapter?.notifyItemChanged(item)
            }
        }

        return binding.root
    }
}