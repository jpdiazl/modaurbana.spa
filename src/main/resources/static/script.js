const API_BASE_URL = 'http://localhost:8081/catalogo-api/api/v1/catalogo';
let allProducts = [];
let currentProducts = [];

// Cargar productos al iniciar
document.addEventListener('DOMContentLoaded', function() {
    loadAllProducts();
    loadStatistics();
});

// Función para realizar peticiones HTTP
async function fetchAPI(endpoint) {
    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching data:', error);
        showError('Error de conexión con el servidor. Verifica que el servicio esté ejecutándose.');
        throw error;
    }
}

// Cargar todos los productos
async function loadAllProducts() {
    try {
        showLoading();
        const products = await fetchAPI('');
        allProducts = products;
        currentProducts = products;
        displayProducts(products);
        hideError();
    } catch (error) {
        showError('Error al cargar los productos');
    }
}

// Cargar productos destacados
async function loadFeaturedProducts() {
    try {
        showLoading();
        const products = await fetchAPI('/destacados');
        currentProducts = products;
        displayProducts(products);
        hideError();
    } catch (error) {
        showError('Error al cargar los productos destacados');
    }
}

// Cargar productos sostenibles
async function loadSustainableProducts() {
    try {
        showLoading();
        const products = await fetchAPI('/sostenibles');
        currentProducts = products;
        displayProducts(products);
        hideError();
    } catch (error) {
        showError('Error al cargar los productos sostenibles');
    }
}

// Buscar productos
async function searchProducts() {
    const query = document.getElementById('searchInput').value.trim();
    if (!query) {
        loadAllProducts();
        return;
    }

    try {
        showLoading();
        const products = await fetchAPI(`/buscar?q=${encodeURIComponent(query)}`);
        currentProducts = products;
        displayProducts(products);
        hideError();
    } catch (error) {
        showError('Error al buscar productos');
    }
}

// Filtrar productos localmente
function filterProducts() {
    const category = document.getElementById('categoryFilter').value;
    const minPrice = parseFloat(document.getElementById('minPrice').value) || 0;
    const maxPrice = parseFloat(document.getElementById('maxPrice').value) || Infinity;
    const sustainable = document.getElementById('sustainableFilter').value;

    let filtered = allProducts.filter(product => {
        const matchesCategory = !category || product.categoria.toLowerCase() === category.toLowerCase();
        const matchesPrice = product.precioActual >= minPrice && product.precioActual <= maxPrice;
        const matchesSustainable = !sustainable || product.sostenible.toString() === sustainable;

        return matchesCategory && matchesPrice && matchesSustainable;
    });

    currentProducts = filtered;
    displayProducts(filtered);
}

// Mostrar productos
function displayProducts(products) {
    const container = document.getElementById('productsContainer');
    
    if (!products || products.length === 0) {
        container.innerHTML = `
            <div class="no-products">
                <i class="fas fa-box-open"></i>
                <h3>No se encontraron productos</h3>
                <p>Intenta modificar los filtros de búsqueda</p>
            </div>
        `;
        return;
    }

    const productsHTML = products.map(product => createProductCard(product)).join('');
    container.innerHTML = `<div class="products-grid">${productsHTML}</div>`;
}

// Crear tarjeta de producto
function createProductCard(product) {
    const badges = [];
    
    if (product.destacado) {
        badges.push('<span class="badge badge-featured">Destacado</span>');
    }
    
    if (product.sostenible) {
        badges.push('<span class="badge badge-sustainable">Sostenible</span>');
    }
    
    if (product.descuento && product.descuento > 0) {
        badges.push(`<span class="badge badge-discount">${product.descuento.toFixed(0)}% OFF</span>`);
    }

    const badgesHTML = badges.length > 0 ? `<div class="product-badges">${badges.join('')}</div>` : '';

    const originalPriceHTML = product.precioOriginal && product.precioOriginal > product.precioActual 
        ? `<span class="price-original">$${product.precioOriginal.toLocaleString()}</span>` 
        : '';

    const tallasHTML = product.tallas && product.tallas.length > 0 
        ? product.tallas.map(talla => `<span class="tag">${talla}</span>`).join('') 
        : '';

    const coloresHTML = product.colores && product.colores.length > 0 
        ? product.colores.map(color => `<span class="tag">${color}</span>`).join('') 
        : '';

    const entrepreneurHTML = product.emprendedor 
        ? `<div class="product-entrepreneur">
             <div class="entrepreneur-label">Emprendedor</div>
             <div class="entrepreneur-name">${product.emprendedor}</div>
           </div>` 
        : '';

    return `
        <div class="product-card">
            <div class="product-image">
                ${badgesHTML}
                <i class="fas fa-tshirt"></i>
            </div>
            <div class="product-info">
                <div class="product-category">${product.categoria}</div>
                <h3 class="product-name">${product.nombre}</h3>
                <p class="product-description">${product.descripcion || 'Sin descripción disponible'}</p>
                
                <div class="product-price">
                    <span class="price-current">$${product.precioActual.toLocaleString()}</span>
                    ${originalPriceHTML}
                </div>

                <div class="product-details">
                    <div class="detail-group">
                        <div class="detail-label">Stock</div>
                        <div class="detail-value">${product.stock} unidades</div>
                    </div>
                    <div class="detail-group">
                        <div class="detail-label">ID</div>
                        <div class="detail-value">#${product.id}</div>
                    </div>
                </div>

                ${(tallasHTML || coloresHTML) ? `
                <div class="product-tags">
                    ${tallasHTML}
                    ${coloresHTML}
                </div>
                ` : ''}

                ${entrepreneurHTML}
            </div>
        </div>
    `;
}

// Cargar estadísticas
async function loadStatistics() {
    try {
        const stats = await fetchAPI('/estadisticas');
        displayStatistics(stats);
    } catch (error) {
        console.error('Error al cargar estadísticas:', error);
    }
}

// Mostrar estadísticas
function displayStatistics(stats) {
    const statsGrid = document.getElementById('statsGrid');
    statsGrid.innerHTML = `
        <div class="stat-card">
            <div class="stat-number">${stats.totalProductos || 0}</div>
            <div class="stat-label">Total Productos</div>
        </div>
        <div class="stat-card">
            <div class="stat-number">${stats.totalSostenibles || 0}</div>
            <div class="stat-label">Productos Sostenibles</div>
        </div>
        <div class="stat-card">
            <div class="stat-number">${stats.totalDestacados || 0}</div>
            <div class="stat-label">Productos Destacados</div>
        </div>
        <div class="stat-card">
            <div class="stat-number">${stats.totalEmprendedores || 0}</div>
            <div class="stat-label">Emprendedores</div>
        </div>
    `;
}

// Mostrar loading
function showLoading() {
    document.getElementById('productsContainer').innerHTML = `
        <div class="loading">
            <i class="fas fa-spinner"></i>
            <div>Cargando productos...</div>
        </div>
    `;
}

// Mostrar error
function showError(message) {
    document.getElementById('errorContainer').innerHTML = `
        <div class="error">
            <i class="fas fa-exclamation-triangle"></i>
            ${message}
        </div>
    `;
}

// Ocultar error
function hideError() {
    document.getElementById('errorContainer').innerHTML = '';
}

// Evento para buscar con Enter
document.getElementById('searchInput').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        searchProducts();
    }
});