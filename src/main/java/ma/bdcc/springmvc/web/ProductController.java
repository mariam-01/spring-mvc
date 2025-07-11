package ma.bdcc.springmvc.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.bdcc.springmvc.entities.Product;
import ma.bdcc.springmvc.repository.ProductRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductRepo productRepo;

    @GetMapping("/all")
    public String getAllProducts(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Product> products;
            if (keyword != null && !keyword.isBlank()) {
                products = productRepo.findByNameContainingIgnoreCase(keyword);
            } else {
                products = productRepo.findAll();
            }
            model.addAttribute("products", products);
            model.addAttribute("keyword", keyword);
            return "products";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/products/all";
    }

    @GetMapping("/admin/add")
    public String showProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "addProduct";
    }

    @PostMapping("/admin/add")
    public String addProduct(@Valid Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "addProduct";

        productRepo.save(product);
        return "redirect:/products/all";
    }

    /*@GetMapping("/{id}")
    public String getProductsById(@PathVariable Long id, Model model) {
        model.addAttribute("product", productRepo.findById(id).orElse(null));
        return "products";
    }*/

    @GetMapping("/admin/update/{id}")
    public String showUpdateProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productRepo.findById(id).orElse(null));
        return "addProduct";
    }

}
