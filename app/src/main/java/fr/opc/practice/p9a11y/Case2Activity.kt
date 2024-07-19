package fr.opc.practice.p9a11y

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat
import com.google.android.material.snackbar.Snackbar
import fr.opc.practice.p9a11y.accessibilityUtils.AccessibilityAction
import fr.opc.practice.p9a11y.databinding.ActivityCase2Binding

/**
 * Activity to display a card of recipe suggestion.
 */
class Case2Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCase2Binding
    private var isFavourite: Boolean = true


    // [LIFE CYCLE] ------------------------------------------------------

    /**
     * Life cycle method - onCreate
     * Method to initialize the activity.
     * @param savedInstanceState The saved instance state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCase2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //pre-setup the favorite button state to off
        removeRecipeFromFavorites()
        //init listeners
        initListeners()
        //init accessibility custom action for slow mode (by default)
        initAccessibilityActions()
        //set the fast mode by default
        accessibilityNavigationFastMode()
        //replace default click accessibility actions
        replaceClickAccessibilityAction()
    }


    // [INITIALIZATION] --------------------------------------------------

    /**
     * Method to initialize listeners.
     */
    private fun initListeners() {

        //Favorite button listener
        binding.favouriteButton.setOnClickListener {
            isFavourite = !isFavourite
            if(!isFavourite) addRecipeToFavorites() else removeRecipeFromFavorites()
        }

        //Add recipe to basket button listener
        binding.addRecipeToBasket.setOnClickListener {
            //Snackbar is better than toast with Talkback ... (Toast speech finish by the name of the app, not the case with snackbar)
            Snackbar.make(this.binding.root, getString(R.string.recette_ajout_au_panier), Snackbar.LENGTH_SHORT).show()
        }

        //Recipe card listener
        binding.recipeCard.setOnClickListener {
            // TODO navigate to recipe screen
            Snackbar.make(this.binding.root,"Voici une delicieuse recette de cookie au chocolat, simple et rapide ! Temps necessaire a la preparation: 15 minutes, Temps necessaire a la cuisson: 10 minutes.", Snackbar.LENGTH_SHORT).show()
        }

    }

    /**
     * Method to initialize all custom accessibility actions.
     */
    private fun initAccessibilityActions(){
        addAccessibilityAction(binding.recipeCard,AccessibilityAction.ADDTOBASKET)
        addAccessibilityAction(binding.recipeCard,AccessibilityAction.FAVORITEADD)
        addAccessibilityAction(binding.recipeCard,AccessibilityAction.FAVORITEREMOVE)
        addAccessibilityAction(binding.recipeCard,AccessibilityAction.SLOWMODE)
        addAccessibilityAction(binding.recipeCard,AccessibilityAction.FASTMODE)
        //set custom content description for the card
        binding.recipeCard.contentDescription = "${getString(R.string.cd_recipe_card)} ${binding.productTitle.text}"
    }


    // [ACCESSIBILITY ACTIONS] -------------------------------------------

    /**
     * Accessibility action ids reference.
     */
    private val accessibilityActionIds = mutableMapOf<AccessibilityAction, Int>() // Map for accessibility actions and their IDs

    /**
     * Method to create a custom accessibility action.
     *
     * @param view The view to add the action.
     * @param action The action enum ref to add.
     * @return The id of the action.
     */
    private fun createAccessibilityAction(view: View, action: AccessibilityAction): Int{

        when(action){
            AccessibilityAction.FASTMODE -> {
                return ViewCompat.addAccessibilityAction(view, getString(R.string.accessibility_action_fastmode)) { _, _ ->
                    accessibilityNavigationFastMode()
                    true
                }
            }
            AccessibilityAction.SLOWMODE -> {
                return ViewCompat.addAccessibilityAction(view, getString(R.string.accessibility_action_slowmode)) { _, _ ->
                    accessibilityNavigationSlowMode()
                    true
                }
            }

            AccessibilityAction.FAVORITEADD -> {
                return ViewCompat.addAccessibilityAction(view, getString(R.string.accessibility_action_addfavorite)) { _, _ ->
                    addRecipeToFavorites()
                    true
                }
            }

            AccessibilityAction.FAVORITEREMOVE -> {
                return ViewCompat.addAccessibilityAction( view,getString(R.string.accessibility_action_removefavorite)) { _, _ ->
                    removeRecipeFromFavorites()
                    true
                }
            }

            AccessibilityAction.ADDTOBASKET -> {
                return ViewCompat.addAccessibilityAction(view,getString(R.string.accessibility_action_addtobasket)) { _, _ ->
                    binding.addRecipeToBasket.performClick()
                    true
                }
            }

            else -> {
                return 0
            }

        }
    }

    /**
     * Method to add custom accessibility action.
     * @param view The view to add the action.
     * @param actionType The type of the action to add.
     */
    private fun addAccessibilityAction(view: View, actionType: AccessibilityAction){
       //add the action to the accessibility action map for the given action "type"
        accessibilityActionIds[actionType] = createAccessibilityAction(view,actionType)
    }

    /**
     * Method to remove custom accessibility action.
     * @param view The view to remove the action.
     * @param actionType The type of the action to remove.
     */
    private fun removeAccessibilityAction(view: View, actionType: AccessibilityAction){
        // remove the action from the accessibility action map for the given action "type"
        ViewCompat.removeAccessibilityAction(view,accessibilityActionIds[actionType]?:0)
        //remove the action from the map
        accessibilityActionIds.remove(actionType)

    }

    /**
     * Method to replace default accessibility actions.
     */
    private fun replaceClickAccessibilityAction(){
        //Action accessibility pour ajouter au panier
        ViewCompat.replaceAccessibilityAction(
            binding.addRecipeToBasket,
            AccessibilityActionCompat.ACTION_CLICK,
            getString(R.string.cd_ajouter),
            null
        )

        //Action accessibility pour consulter la card
        ViewCompat.replaceAccessibilityAction(
            binding.recipeCard,
            AccessibilityActionCompat.ACTION_CLICK,
            getString(R.string.cd_consult_recipe),
            null
        )

    }



    // [ACCESSIBILITY NAVIGATION MODES] ----------------------------------

    /**
     * Method to set the accessibility navigation mode to fast mode.
     */
    private fun accessibilityNavigationFastMode() {
        //In fast mode only the recipe card is read + available custom accessibility actions for this mode
        //a slide to the next accessibility field conduct to the next recipe
        //unlock/lock the views elements to get this behavior
        binding.recipeCard.importantForAccessibility = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES
        binding.favouriteButton.importantForAccessibility = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO
        binding.addRecipeToBasket.importantForAccessibility = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO
        binding.productTitle.importantForAccessibility = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO

        //Remove fast mode action
        removeAccessibilityAction(binding.recipeCard,AccessibilityAction.FASTMODE)

        //Add slow mode action, and other actions
        addAccessibilityAction(binding.recipeCard,AccessibilityAction.ADDTOBASKET)
        addAccessibilityAction(binding.recipeCard,AccessibilityAction.FAVORITEADD)
        addAccessibilityAction(binding.recipeCard,AccessibilityAction.FAVORITEREMOVE)
        addAccessibilityAction(binding.recipeCard,AccessibilityAction.SLOWMODE)

        //announce fast mode for accessibility
        binding.recipeCard.announceForAccessibility(getString(R.string.fast_mode_navigation_accessibility_announce))

    }

    /**
     * Method to set the accessibility navigation mode to detailed mode.
     */
    private fun accessibilityNavigationSlowMode() {
        //In Slow mode all view elements are read
        //a slide to the next accessibility field conduct to the next field inside the recipe card
        //unlock/lock the views elements to get this behavior
        binding.recipeCard.importantForAccessibility = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES
        binding.favouriteButton.importantForAccessibility = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES
        binding.addRecipeToBasket.importantForAccessibility = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES
        binding.productTitle.importantForAccessibility = ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES

        //Remove slow mode action + old useless action in slow mode
        removeAccessibilityAction(binding.recipeCard,AccessibilityAction.SLOWMODE)
        removeAccessibilityAction(binding.recipeCard,AccessibilityAction.ADDTOBASKET)
        removeAccessibilityAction(binding.recipeCard,AccessibilityAction.FAVORITEADD)
        removeAccessibilityAction(binding.recipeCard,AccessibilityAction.FAVORITEREMOVE)

        //add custom action for the fast mode
        addAccessibilityAction(binding.recipeCard,AccessibilityAction.FASTMODE)

        //announce slow mode for accessibility
        binding.recipeCard.announceForAccessibility(getString(R.string.slow_mode_navigation_accessibility_announce))

    }


    // [FAVORITE STATES] -------------------------------------------------

    /**
     * Method to add recipe to favorites.
     */
    private fun addRecipeToFavorites(){
        binding.favouriteButton.setImageResource(R.drawable.ic_favourite_on)
        ViewCompat.replaceAccessibilityAction(
            binding.favouriteButton,
            AccessibilityActionCompat.ACTION_CLICK,
            getString(R.string.action_unfavorite),
            null
        )
        binding.favouriteButton.announceForAccessibility(getString(R.string.cd_ajouter_aux_favoris))
    }

    /**
     * Method to remove recipe from favorites.
     */
    private fun removeRecipeFromFavorites(){
        binding.favouriteButton.setImageResource(R.drawable.ic_favourite_off)
        ViewCompat.replaceAccessibilityAction(
            binding.favouriteButton,
            AccessibilityActionCompat.ACTION_CLICK,
            getString(R.string.action_favorite),
            null
        )
        binding.favouriteButton.announceForAccessibility(getString(R.string.cd_retirer_des_favoris))
    }


}
