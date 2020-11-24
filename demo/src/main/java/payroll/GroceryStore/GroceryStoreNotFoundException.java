package payroll.GroceryStore;

class GroceryStoreNotFoundException extends RuntimeException {

	GroceryStoreNotFoundException(Long id) {
        super("Could not find Grocery Store " + id);
    }
}