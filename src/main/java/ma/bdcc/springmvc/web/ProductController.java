package ma.bdcc.springmvc.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.bdcc.springmvc.entities.Product;
import ma.bdcc.springmvc.repository.ProductRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductRepo productRepo;

    @GetMapping()
    public String getAllProducts(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("/add")
    public String showProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "addProduct";
    }

    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "addProduct";

        productRepo.save(product);
        return "redirect:/products";
    }

    /*@GetMapping("/{id}")
    public String getProductsById(@PathVariable Long id, Model model) {
        model.addAttribute("product", productRepo.findById(id).orElse(null));
        return "products";
    }*/

    @PostMapping("/update")
    public String updateProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productRepo.findById(id).orElse(null));
        return "addProduct";
    }

}
