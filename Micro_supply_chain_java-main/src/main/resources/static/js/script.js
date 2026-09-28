// Micro-Enterprise Supply Chain & Delivery Optimizer JS

document.addEventListener('DOMContentLoaded', function () {
    initOrderForm();
    initRoutePlanner();
    initAlertDismiss();
});

// Dynamic Order Form line items & live total calculation
function initOrderForm() {
    const itemsTable = document.getElementById('orderItemsTable');
    const addItemBtn = document.getElementById('addItemBtn');
    const grandTotalSpan = document.getElementById('grandTotalDisplay');

    if (!itemsTable || !addItemBtn) return;

    function calculateGrandTotal() {
        let total = 0;
        const rows = itemsTable.querySelectorAll('tbody tr');
        rows.forEach(row => {
            const select = row.querySelector('.product-select');
            const qtyInput = row.querySelector('.quantity-input');
            const subtotalCell = row.querySelector('.subtotal-cell');

            if (select && qtyInput && select.value) {
                const selectedOption = select.options[select.selectedIndex];
                const price = parseFloat(selectedOption.getAttribute('data-price') || '0');
                const qty = parseInt(qtyInput.value || '0', 10);
                const subtotal = price * qty;
                if (subtotalCell) {
                    subtotalCell.textContent = '₹' + subtotal.toFixed(2);
                }
                total += subtotal;
            } else if (subtotalCell) {
                subtotalCell.textContent = '₹0.00';
            }
        });

        if (grandTotalSpan) {
            grandTotalSpan.textContent = '₹' + total.toFixed(2);
        }
    }

    // Attach listeners to initial row
    itemsTable.addEventListener('change', function (e) {
        if (e.target.classList.contains('product-select') || e.target.classList.contains('quantity-input')) {
            const row = e.target.closest('tr');
            const select = row.querySelector('.product-select');
            const stockNotice = row.querySelector('.stock-notice');
            if (select && stockNotice && select.value) {
                const opt = select.options[select.selectedIndex];
                const stock = opt.getAttribute('data-stock');
                stockNotice.textContent = `Available Stock: ${stock}`;
            }
            calculateGrandTotal();
        }
    });

    itemsTable.addEventListener('input', function (e) {
        if (e.target.classList.contains('quantity-input')) {
            calculateGrandTotal();
        }
    });

    // Remove row button
    itemsTable.addEventListener('click', function (e) {
        if (e.target.closest('.remove-row-btn')) {
            const rows = itemsTable.querySelectorAll('tbody tr');
            if (rows.length > 1) {
                e.target.closest('tr').remove();
                reindexRows();
                calculateGrandTotal();
            } else {
                alert('Order must contain at least one item.');
            }
        }
    });

    // Add row button
    addItemBtn.addEventListener('click', function () {
        const tbody = itemsTable.querySelector('tbody');
        const rows = tbody.querySelectorAll('tr');
        const newIndex = rows.length;

        const firstRow = rows[0];
        const newRow = firstRow.cloneNode(true);

        // Reset values
        const select = newRow.querySelector('.product-select');
        select.name = `items[${newIndex}].productId`;
        select.value = '';

        const qtyInput = newRow.querySelector('.quantity-input');
        qtyInput.name = `items[${newIndex}].quantity`;
        qtyInput.value = '1';

        const stockNotice = newRow.querySelector('.stock-notice');
        if (stockNotice) stockNotice.textContent = 'Available Stock: -';

        const subtotalCell = newRow.querySelector('.subtotal-cell');
        if (subtotalCell) subtotalCell.textContent = '₹0.00';

        tbody.appendChild(newRow);
        calculateGrandTotal();
    });

    function reindexRows() {
        const rows = itemsTable.querySelectorAll('tbody tr');
        rows.forEach((row, idx) => {
            const select = row.querySelector('.product-select');
            const qtyInput = row.querySelector('.quantity-input');
            if (select) select.name = `items[${idx}].productId`;
            if (qtyInput) qtyInput.name = `items[${idx}].quantity`;
        });
    }

    calculateGrandTotal();
}

// Route Planner select all checkboxes
function initRoutePlanner() {
    const selectAll = document.getElementById('selectAllDeliveries');
    const deliveryCheckboxes = document.querySelectorAll('.delivery-checkbox');

    if (selectAll) {
        selectAll.addEventListener('change', function () {
            deliveryCheckboxes.forEach(cb => {
                cb.checked = selectAll.checked;
            });
        });
    }
}

// Auto dismiss alerts after 5 seconds
function initAlertDismiss() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 6000);
    });
}
