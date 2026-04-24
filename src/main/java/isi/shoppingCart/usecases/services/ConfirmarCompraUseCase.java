package isi.shoppingCart.usecases.services;

import isi.shoppingCart.entities.Cart;
import isi.shoppingCart.entities.CartItem;
import isi.shoppingCart.entities.Product;
import isi.shoppingCart.entities.Purchase;
import isi.shoppingCart.usecases.ports.CartRepository;
import isi.shoppingCart.usecases.ports.ProductRepository;
import isi.shoppingCart.usecases.ports.PurchaseRepository;

public class ConfirmarCompraUseCase {
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private PurchaseRepository purchaseRepository;

    public ConfirmarCompraUseCase(CartRepository cartRepository,
            ProductRepository productRepository,
            PurchaseRepository purchaseRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.purchaseRepository = purchaseRepository;
    }

    public Purchase execute() {
        // 1. Recuperar el carrito
        Cart cart = cartRepository.getCart();

        // 2. Verificar que no esté vacío
        if (cart.isEmpty()) {
            return null;
        }

        // 3. Crear la compra (snapshot de items + total + fecha)
        Purchase purchase = new Purchase(cart.getItems(), cart.getTotal());

        // 4. Actualizar stock de cada producto
        for (CartItem item : cart.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId());
            for (int i = 0; i < item.getQuantity(); i++) {
                product.decreaseAvailableQuantity(); // ya existe en Product
            }
            productRepository.save(product); // ← nuevo método necesario en el puerto
        }

        // 5. Persistir la compra
        purchaseRepository.save(purchase);

        // 6. Vaciar el carrito
        cartRepository.clear();

        // 7. Devolver confirmación
        return purchase;
    }
}
