package fr.opc.practice.p9a11y

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import fr.opc.practice.p9a11y.databinding.ActivityCase3Binding

/**
 * Activity to the case 3 : accessibility on text input field
 */
class Case3Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCase3Binding

    /**
     * Lifecycle method called when the activity is created
     * @param savedInstanceState the saved instance state
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCase3Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //disable the validate button by default
        binding.validateButton.isEnabled = false
        //add a text watcher
        binding.pseudoEdittext.addTextChangedListener {
            //get the text length and apply the validation/error UI
            if(it.toString().length > 2) showSuccess(binding.pseudoEditlayout) else showError(binding.pseudoEditlayout)
        }
    }


    /**
     * Method to show an error input UI
     * @param textInputLayout the input layout to update
     */
    private fun showError(textInputLayout: TextInputLayout) {
        binding.pseudoEditlayout.error = getString(R.string.error_pseudonyme_field)
        textInputLayout.isErrorEnabled = true
        textInputLayout.hintTextColor = ColorStateList.valueOf(resources.getColor(R.color.red400, null))
        textInputLayout.boxStrokeColor = resources.getColor(R.color.red400, null)
        binding.validateButton.isEnabled = false
    }

    /**
     * Method to show a success input UI
     * @param textInputLayout the input layout to update
     */
    private fun showSuccess(textInputLayout: TextInputLayout) {
        binding.pseudoEditlayout.error = null
        textInputLayout.isErrorEnabled = false
        textInputLayout.hintTextColor = ColorStateList.valueOf(resources.getColor(R.color.green400, null))
        textInputLayout.boxStrokeColor = resources.getColor(R.color.green400, null)
        textInputLayout.endIconMode = TextInputLayout.END_ICON_CUSTOM
        textInputLayout.endIconDrawable = AppCompatResources.getDrawable(this,R.drawable.baseline_check_circle_24)
        textInputLayout.setEndIconTintList(ColorStateList.valueOf(resources.getColor(R.color.green400, null)))
        binding.validateButton.isEnabled = true
    }

}
