package payroll.GroceryStore;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "GROCERY_STORE_ITEMS")
public class Item {

	private @Id @GeneratedValue Long id;
	
	@Column(name = "name_item")
	String name;
	
	public GroceryStore getG() {
		return g;
	}

	public void setG(GroceryStore g) {
		this.g = g;
	}

	@Column(name = "Category_number")
	int category;
	
	@Column(name = "Expiry_Date")
	Date expiryDate;
	
	@ManyToOne
	private GroceryStore g;
	
	
	
	public Item() {
	}

	public Item(String name, int category, Date expiryDate) {
		this.name = name;
		this.category = category;
		this.expiryDate = expiryDate;
	}
	
	public Long getId() {
		return id;
	}
	

    public void setId(Long id) {
        this.id = id;
    }

	public Date getExpiryDate() {
		return expiryDate;
	}
	
	public void setExpiryDate(Date e) {
		expiryDate = e;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}

	@Override
	public int hashCode() {
		return Objects.hash(category, expiryDate, g, id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Item))
			return false;
		Item other = (Item) obj;
		return category == other.category && Objects.equals(expiryDate, other.expiryDate)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", category=" + category + ", expiryDate=" + expiryDate
				 + "]";
	}
	
	
	
}
