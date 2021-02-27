package com.amirhusseinsoori.adavancetesting.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.amirhusseinsoori.adavancetesting.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePickFragment:Fragment(R.layout.fragment_image_pick) {
    val viewModel: ShoppingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}