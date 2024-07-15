package fr.opc.practice.p9a11y

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fr.opc.practice.p9a11y.databinding.ActivityCase1Binding

/**
 * The activity for the first case - Being able to add a certain quantity of a product to my basket.
 */
class Case1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCase1Binding
    private var quantity = 0

    /**
     * Life cycle method - Called when the activity is created.
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCase1Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        updateUIQuantity(quantity)
        initListeners()
    }

    /**
     * Method to initialize the listeners on the buttons.
     */
    private fun initListeners() {
        binding.addButton.setOnClickListener { incrementQuantity() }
        binding.removeButton.setOnClickListener { decrementQuantity() }
    }

    /**
     * Method to increment the quantity of the product in the basket.
     */
    private fun incrementQuantity() {
        quantity++
        updateUIQuantity(quantity)
    }

    /**
     * Method to decrement the quantity of the product in the basket.
     */
    private fun decrementQuantity() {
        if (quantity > 0) {
            quantity--
            updateUIQuantity(quantity)
        } else {
            Toast.makeText(this, getString(R.string.impossible_d_avoir_une_quantit_n_gative), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Method to update the UI with the quantity of the product in the basket.
     * @param quantity The quantity of the product in the basket.
     */
    private fun updateUIQuantity(quantity: Int){ binding.quantityText.text = "$quantity"}


}
