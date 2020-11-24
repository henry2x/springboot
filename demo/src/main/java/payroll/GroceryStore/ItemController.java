package payroll.GroceryStore;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Controller
@EnableWebMvc
class ItemController {

    private final ItemRepository repository;
    private ItemModelAssembler assembler;
    
    ItemController(ItemRepository repository, ItemModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
        
    }

    @RequestMapping("/itemController")
    public String home(Model model) {
    	model.addAttribute("items", repository.findAll());
    	return "index";
    }
    @PostMapping("/saveItem")
    public String saveItem(@ModelAttribute("items") Item item) {
    	repository.save(item);
    	return "redirect:/";
    }

    // Aggregate root

    @GetMapping("/Items")
    CollectionModel<EntityModel<Item>> all() {

        List<EntityModel<Item>> items = repository.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList()); 
    	

        return CollectionModel.of(items, linkTo(methodOn(ItemController.class).all()).withSelfRel());
    }

    @PostMapping("/Items")
    ResponseEntity<?> newItem(@RequestBody Item newItem) {

        EntityModel<Item> entityModel = assembler.toModel(repository.save(newItem));

        return ResponseEntity 
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) 
                .body(entityModel);
    }

    // Single item
    @GetMapping("/Items/{id}")
    EntityModel<Item> one(@PathVariable Long id) {

        Item Item = repository.findById(id) //
                .orElseThrow(() -> new ItemNotFoundException(id));

        return assembler.toModel(Item);
    }

    @PutMapping("/Items/{id}")
    ResponseEntity<?> replaceItem(@RequestBody Item newItem, @PathVariable Long id) {

    	Item updatedItem = repository.findById(id)
                .map(Item -> {
                	Item.setCategory(newItem.getCategory());
                	Item.setExpiryDate(newItem.getExpiryDate());
                	Item.setName(newItem.getName());
                    return repository.save(Item);
                })
                .orElseGet(() -> {
                	newItem.setId(id);
                    return repository.save(newItem);
                });
        
        EntityModel<Item> entityModel = assembler.toModel(updatedItem);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        
    }

    @DeleteMapping("/Items/{id}")
    ResponseEntity<?> deleteItem(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}