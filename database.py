import mysql.connector
from mysql.connector import Error

DB_CONFIG = {
    'host': 'localhost',
    'user': 'root',
    'password': 'admin',
    'database': 'MajikkuStores'
}

def get_db_connection():
    try:
        return mysql.connector.connect(**DB_CONFIG)
    except Error as e:
        print(f"Error connecting to MySQL: {e}")
        return None

# --- AUTH & PERMISSIONS ---
def verify_user_credentials(employee_id):
    conn = get_db_connection()
    if not conn: return None
    try:
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT PasswordHash FROM EmployeeCredentials WHERE EmployeeID = %s", (employee_id,))
        return cursor.fetchone()
    finally:
        conn.close()

def get_user_permissions(employee_id):
    conn = get_db_connection()
    if not conn: return []
    try:
        cursor = conn.cursor()
        query = """
            SELECT p.PermissionName FROM Permissions p
            JOIN JobPermissions jp ON p.PermissionID = jp.PermissionID
            JOIN Employees e ON e.JobID = jp.JobID
            WHERE e.EmployeeID = %s
        """
        cursor.execute(query, (employee_id,))
        return [row[0] for row in cursor.fetchall()]
    finally:
        conn.close()

# --- ADMIN/HR MANAGEMENT ---
def get_manageable_users(admin_id):
    conn = get_db_connection()
    if not conn: return []
    try:
        cursor = conn.cursor(dictionary=True)
        # Fetch data: Employee Name + Job Title + Store Address
        query = """
            SELECT e.EmployeeID, e.FirstName, e.LastName, jt.TitleName, s.Address as Store 
            FROM Employees e 
            JOIN JobTitles jt ON e.JobID = jt.JobID 
            JOIN Stores s ON e.StoreID = s.StoreID
        """
        cursor.execute(query)
        return cursor.fetchall()
    finally:
        conn.close()

def get_all_jobs():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT JobID, TitleName FROM JobTitles")
    res = cursor.fetchall()
    conn.close()
    return res

def update_employee_job(emp_id, job_id):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("UPDATE Employees SET JobID = %s WHERE EmployeeID = %s", (job_id, emp_id))
    conn.commit()
    conn.close()

# --- INVENTORY CRUD ---
def get_inventory_list():
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    query = """
        SELECT si.InventoryID, p.Name, p.SKU, si.QuantityOnHand, si.ReorderLevel
        FROM StoreInventory si
        JOIN Products p ON si.ProductID = p.ProductID
    """
    cursor.execute(query)
    res = cursor.fetchall()
    conn.close()
    return res

def add_new_product(name, sku, unit_price, qty):
    conn = get_db_connection()
    cursor = conn.cursor()
    try:
        # Defaults for prototype: SubCat=1, Brand=1, Store=1
        cursor.execute("INSERT INTO Products (SubCategoryID, BrandID, Name, UnitPrice, SKU) VALUES (1, 1, %s, %s, %s)",
                       (name, unit_price, sku))
        prod_id = cursor.lastrowid
        cursor.execute("INSERT INTO StoreInventory (StoreID, ProductID, QuantityOnHand) VALUES (1, %s, %s)",
                       (prod_id, qty))
        conn.commit()
    finally:
        conn.close()

def update_stock(inv_id, qty):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("UPDATE StoreInventory SET QuantityOnHand = %s WHERE InventoryID = %s", (qty, inv_id))
    conn.commit()
    conn.close()

def delete_inventory_item(inv_id):
    conn = get_db_connection()
    cursor = conn.cursor()
    cursor.execute("DELETE FROM StoreInventory WHERE InventoryID = %s", (inv_id,))
    conn.commit()
    conn.close()

# --- DYNAMIC TABLE ENGINE ---
def get_any_table_data(table_name):
    conn = get_db_connection()
    if not conn: return [], []
    try:
        cursor = conn.cursor(dictionary=True)
        cursor.execute(f"SELECT * FROM {table_name} LIMIT 100")
        rows = cursor.fetchall()
        columns = rows[0].keys() if rows else []
        return columns, rows
    except Exception as e:
        print(f"DB Error: {e}")
        return [], []
    finally:
        conn.close()