package com.assign.androidinterntask.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.assign.androidinterntask.R
import com.assign.androidinterntask.adapters.LinksAdapter
import com.assign.androidinterntask.databinding.FragmentHomeBinding
import com.assign.androidinterntask.model.DashboardDetails
import com.assign.androidinterntask.model.LinkDetails
import com.assign.androidinterntask.utils.Resource
import com.assign.androidinterntask.utils.Status
import com.assign.androidinterntask.viewmodels.DashboardViewModel
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.util.Calendar
import java.util.Collections


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var observerBanner: Observer<Resource<DashboardDetails>>
    private var dashboardDetails: DashboardDetails? = null
    private var adapter: LinksAdapter? = null

    private val dashboardViewModel: DashboardViewModel by activityViewModels()

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initView()
        handleListeners()
    }

    private fun handleListeners() {
        binding.headingRecentLinks.setOnClickListener {
            resetHeadings()
            binding.headingRecentLinks.setBackgroundTintList(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.blue
                    )
                )
            )
            binding.headingRecentLinks.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            adapter?.setList(getRecentLinks(dashboardDetails))
            adapter?.notifyDataSetChanged()
        }
        binding.headingTopLinks.setOnClickListener {
            resetHeadings()
            binding.headingTopLinks.setBackgroundTintList(
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.blue
                    )
                )
            )
            binding.headingTopLinks.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            adapter?.setList(getTopLinks(dashboardDetails))
            adapter?.notifyDataSetChanged()
        }
    }

    private fun resetHeadings() {

        binding.headingRecentLinks.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.transparent
                )
            )
        )
        binding.headingTopLinks.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.transparent
                )
            )
        )
        binding.headingRecentLinks.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_grey_text_color
            )
        )
        binding.headingTopLinks.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_grey_text_color
            )
        )
    }

    private fun initData() {
        observerBanner =
            Observer<Resource<DashboardDetails>> { resource: Resource<DashboardDetails?> ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        dashboardDetails = resource.data
                        updateUI()
                    }

                    Status.ERROR -> {
                        Toast.makeText(
                            requireContext(),
                            "Error in fetching details.",
                            Toast.LENGTH_SHORT
                        )
                    }

                    Status.LOADING -> {}
                    else -> {

                    }
                }
            }
        dashboardViewModel.getNewDashboard().observe(viewLifecycleOwner, observerBanner)
    }

    private fun initView() {
        setTopCards()
        binding.greeting.setText(getGreetingText())
    }

    private fun setTopCards() {
        binding.todaysClicksParent.icon.setImageResource(R.drawable.clicks);
        binding.topLocationParent.icon.setImageResource(R.drawable.pin);
        binding.topSourceParent.icon.setImageResource(R.drawable.globe);
        binding.bestTimeParent.icon.setImageResource(R.drawable.time);

        binding.todaysClicksParent.icon.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.purple_light
                )
            )
        )
        binding.topSourceParent.icon.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red_light
                )
            )
        )
        binding.topLocationParent.icon.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.blue_light_2
                )
            )
        )
        binding.bestTimeParent.icon.setBackgroundTintList(
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.yellow_light
                )
            )
        )

        getGraphData()
        makeLineGraph()
    }

    private fun updateUI() {
        binding.todaysClicksParent.title.setText(dashboardDetails?.today_clicks.toString())
        binding.topLocationParent.title.setText(dashboardDetails?.top_location.toString())
        binding.topSourceParent.title.setText(dashboardDetails?.top_source.toString())
        binding.bestTimeParent.title.setText(dashboardDetails?.startTime.toString())

        dashboardDetails?.let { dashboardDetails ->
            initRecyclerView(getTopLinks(dashboardDetails))
        }

        // You can set the name from here, once the details from api are available
        //binding.name.setText(dashboardDetails?.name)

    }

    private fun getRecentLinks(dashboardDetails: DashboardDetails?): ArrayList<LinkDetails> {
        dashboardDetails?.let { dashboardDetails ->
            var links = dashboardDetails.getRecentLinks()
            Collections.sort(
                links,
                Comparator<LinkDetails> { o1, o2 -> (o2.createdAt.compareTo(o1.createdAt)) })
            return links
        }
        return ArrayList()
    }

    private fun getTopLinks(dashboardDetails: DashboardDetails?): ArrayList<LinkDetails> {
        dashboardDetails?.let { dashboardDetails ->
            var links = dashboardDetails.getRecentLinks()
            links.sortByDescending { it.totalClicks }
            return links
        }
        return ArrayList()
    }

    private fun initRecyclerView(links: ArrayList<LinkDetails>) {
        adapter = LinksAdapter(requireContext(), links)
        binding.recyclerViewLinks.setAdapter(adapter)

    }

    private fun getGreetingText(): String {
        val hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hours >= 5 && hours < 12 -> "Good morning"
            hours >= 12 && hours < 18 -> "Good afternoon"
            hours >= 18 && hours < 22 -> "Good evening"
            else -> "Good night"
        }
    }


    private fun getGraphData() {
        //Doing on Temporary Available Data for the time being
        //Not able to get Graph Data from the Api response, the json is not formatted properly, change the response of json value of overall_url_chart to [] list
    }

    private fun makeLineGraph() {
        val dataPoints = mapOf(
            "2023-05-14" to 0f,
            "2023-05-15" to 0f,
            "2023-05-16" to 0f,
            "2023-05-17" to 0f,
            "2023-05-18" to 0f,
            "2023-05-19" to 9f,
            "2023-05-20" to 4f,
            "2023-05-21" to 1f,
            "2023-05-22" to 10f,
            "2023-05-23" to 7f,
            "2023-05-24" to 9f,
            "2023-05-25" to 0f,
            "2023-05-26" to 2f,
            "2023-05-27" to 2f,
            "2023-05-28" to 1f,
            "2023-05-29" to 0f,
            "2023-05-30" to 2f,
            "2023-05-31" to 1f,
            "2023-06-01" to 1f,
            "2023-06-02" to 5f,
            "2023-06-03" to 1f,
            "2023-06-04" to 2f,
            "2023-06-05" to 19f,
            "2023-06-06" to 0f,
            "2023-06-07" to 3f,
            "2023-06-08" to 5f,
            "2023-06-09" to 11f,
            "2023-06-10" to 4f,
            "2023-06-11" to 9f,
            "2023-06-12" to 4f,
            "2023-06-13" to 1f
        )

        val entries = dataPoints.entries
            .sortedBy { it.key }
            .mapIndexed { index, entry ->
                Entry(index.toFloat(), entry.value)
            }

        val dataSet = LineDataSet(entries, "").apply {
            color = ContextCompat.getColor(requireContext(), R.color.blue)
            valueTextColor = ContextCompat.getColor(requireContext(), R.color.light_grey_text_color)
            setDrawCircles(false)
            setDrawValues(false)
            setDrawFilled(true)
        }


        val lineData = LineData(dataSet)
        binding.graphViewHolder.description = Description().apply {
            text = ""
        }

        binding.graphViewHolder.legend.isEnabled = false
        binding.graphViewHolder.data = lineData

        val yAxis = binding.graphViewHolder.axisLeft
        yAxis.gridColor = ContextCompat.getColor(requireContext(), R.color.light_grey_grid_color)
        binding.graphViewHolder.axisRight.isEnabled = false
        binding.graphViewHolder.axisLeft.axisMinimum = 0f

        val monthNames = arrayOf("May", "June")
        val xAxis = binding.graphViewHolder.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.gridColor = ContextCompat.getColor(requireContext(), R.color.light_grey_grid_color)
        xAxis.valueFormatter = IndexAxisValueFormatter(monthNames)
        xAxis.granularity = 2f
        xAxis.labelCount = dataPoints.size
        binding.graphViewHolder.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.graphViewHolder.isHighlightPerTapEnabled = false

        binding.graphViewHolder.animateY(1000)
        binding.graphViewHolder.invalidate()
    }


}