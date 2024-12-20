

# **EasyShop API**

## **Project Overview**

The **EasyShop API** is a RESTful service designed to power an online store. It allows users to:
- Browse products in the store.
- Add or remove items from their shopping cart.
- Complete purchases.

It also includes features for administrators, such as:
- Organizing products into categories.
- Adding, updating, or deleting products in the store.

Think of it as the "behind-the-scenes worker" that keeps the store running smoothly.

---

## **Key Features**
- User Registration and Login.
- Category Management for admins.
- Planned shopping cart and profile management.
- RESTful API endpoints for seamless interaction.

---

### **Visual Overview**

![EasyShop Logo](https://github.com/user-attachments/assets/832a0563-7600-4e1e-b0e0-34b9bb4b7501)

---

## **API Endpoints**

### **Implemented Endpoints**
| **Method** | **Endpoint**              | **Description**                                                                 |
|------------|---------------------------|---------------------------------------------------------------------------------|
| POST       | `/Register`               | Register a new user.                                                           |
| POST       | `/Login - new user`       | Log in as a standard user.                                                     |
| POST       | `/Login - as admin`       | Log in with admin privileges.                                                  |
| GET        | `/Get Categories`         | Fetch all categories.                                                          |
| POST       | `/Add Category - not admin` | Test adding a category as a non-admin (expected to fail).                      |
| GET        | `/Get Categories`         | Recheck all categories (likely post-test).                                     |

### **Green Test Indicators**
- **1**: The test passed successfully.
- **0**: No failures occurred.

All actions were successfully tested, ensuring the API behaves as expected.

---

## **Phases Overview**

### **Phase 1: Categories Controller**
- **Features**: 
  - Fetch all categories.
  - Confirm admin-only access to add categories.
- **Endpoints**:
  - `GET /categories`
  - `POST /categories` (admin-only)
- **Completion**: ✅ Implemented and tested successfully.

![Phase 1 Image](https://github.com/user-attachments/assets/2600bd63-cf8e-4a5f-b612-47453f88161b)

---

### **Phase 2: Bug Fix Testing**
- **Bug 1**: Fixed incorrect product search results based on filters like category, price range, and color.
- **Bug 2**: Resolved duplicate product creation during updates.
- **Notable Code**:  
  - Dynamically fetches products from the database using flexible filters.  

![Phase 2 Image](https://github.com/user-attachments/assets/9f68fee7-978e-47ee-b8a5-8c5e5e51ad02)

---

### **Phase 3: Shopping Cart (Not Implemented)**
**Planned Features**:
- Add products to a shopping cart.
- Update product quantities.
- Clear the shopping cart.

#### **Planned Endpoints**:
| **Method** | **Endpoint**                     | **Description**                             |
|------------|----------------------------------|---------------------------------------------|
| GET        | `/cart`                          | Retrieve the shopping cart for the user.    |
| POST       | `/cart/products/{id}`            | Add a product to the shopping cart.         |
| PUT        | `/cart/products/{id}`            | Update the quantity of a product in the cart. |
| DELETE     | `/cart`                          | Clear the shopping cart for the user.       |

---

### **Phase 4: User Profile (Not Implemented)**
**Planned Features**:
- Users can view and update their profiles.
- A profile record is created during registration.

#### **Planned Endpoints**:
| **Method** | **Endpoint**       | **Description**                |
|------------|--------------------|--------------------------------|
| GET        | `/profile`         | Retrieve the user's profile.   |
| PUT        | `/profile`         | Update the user's profile.     |

---

### **Phase 5: Checkout (Not Implemented)**
**Planned Features**:
- Allow users to convert their shopping cart into an order.
- Generate order details for each item in the cart.

#### **Planned Endpoint**:
| **Method** | **Endpoint**       | **Description**                                  |
|------------|--------------------|-------------------------------------------------|
| POST       | `/orders`          | Create a new order from the shopping cart.      |

---

## **Summary**
### **What’s Completed**
- **Phase 1**: Categories Controller for admin functionality.
- **Login & Registration**: Fully functional for both users and admins.
- **Phase 2**: Fixed bugs related to product search and duplication.

### **What’s Missing**
- **Phase 2 Testing**: Complete CRUD tests for products.
- **Phases 3-5**: Shopping cart, user profile, and checkout functionalities remain unimplemented.

---

## **How to Improve**
1. **Phase 2 Testing**:
   - Test product search filters for accuracy.
   - Ensure no duplicates during product updates.
2. **Implement Remaining Phases**:
   - Add shopping cart functionality.
   - Build user profile management.
   - Develop the checkout process.

---

This README provides a clear and visually appealing representation of the EasyShop API for reviewers and developers. Let me know if you’d like to refine it further!
