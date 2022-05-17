package com.tossdesu.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tossdesu.shoppinglist.R
import com.tossdesu.shoppinglist.databinding.FragmentShopItemBinding
import com.tossdesu.shoppinglist.domain.ShopItem

class ShopItemFragment : Fragment() {

    private lateinit var binding: FragmentShopItemBinding
    private lateinit var viewModel: ShopItemViewModel
    private lateinit var onEditingCompleteListener: OnEditingCompleteListener

    private var screenMode: String = MODE_UNDEFINED
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("LifecycleTest", "ShopItemFragment -> ATTACH")
        if (context is OnEditingCompleteListener) {
            onEditingCompleteListener = context
        } else {
            throw RuntimeException("Activity must implement CompleteEditingItemListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifecycleTest", "ShopItemFragment -> CREATE")
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("LifecycleTest", "ShopItemFragment -> CREATE_VIEW")
        binding = FragmentShopItemBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LifecycleTest", "ShopItemFragment -> VIEW_CREATED")
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        launchScreenMode()
        addTextChangedListeners()
        observeViewModel()
    }


    override fun onStart() {
        super.onStart()
        Log.d("LifecycleTest", "ShopItemFragment -> START")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifecycleTest", "ShopItemFragment -> RESUME")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifecycleTest", "ShopItemFragment -> PAUSE")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifecycleTest", "ShopItemFragment -> STOP")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("LifecycleTest", "ShopItemFragment -> DESTROY_VIEW")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifecycleTest", "ShopItemFragment -> DESTROY")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("LifecycleTest", "ShopItemFragment -> DETACH")
    }

    interface OnEditingCompleteListener {
        fun onEditingComplete()
    }

    private fun observeViewModel() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.input_name_error)
            } else {
                null
            }
            binding.tilName.error = message
        }
        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.input_count_error)
            } else {
                null
            }
            binding.tilCount.error = message
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingCompleteListener.onEditingComplete()
        }
    }

    private fun addTextChangedListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        binding.etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun launchScreenMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchEditMode() {
        viewModel.shopItem.observe(viewLifecycleOwner) {
            binding.etName.setText(it.name)
            binding.etCount.setText(it.count.toString())
        }
        viewModel.getShopItem(shopItemId)

        binding.buttonSave.setOnClickListener {
            viewModel.editShopItem(binding.etName.text.toString(), binding.etCount.text.toString())
        }
    }

    private fun launchAddMode() {
        binding.buttonSave.setOnClickListener {
            viewModel.addShopItem(binding.etName.text.toString(), binding.etCount.text.toString())
        }
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param extra mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_EDIT && mode != MODE_ADD) {
            throw RuntimeException("Unknown param screen mode: $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param extra shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    companion object {

        private const val SCREEN_MODE = "mode"
        private const val SHOP_ITEM_ID = "shop_item_id"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNDEFINED = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}