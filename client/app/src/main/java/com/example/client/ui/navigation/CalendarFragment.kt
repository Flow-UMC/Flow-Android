package com.example.client.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.client.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {
    private lateinit var viewBinding: FragmentCalendarBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentCalendarBinding.inflate(layoutInflater)
        return viewBinding.root
    }
}