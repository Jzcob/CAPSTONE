from flask import Flask, render_template, request, redirect, url_for, session, flash
import hashlib
import database as db

app = Flask(__name__)
app.secret_key = 'majikku_secret_key'

@app.route('/')
def login():
    if 'user_id' in session: return redirect(url_for('dashboard'))
    return render_template('login.html')

@app.route('/login', methods=['POST'])
def handle_login():
    emp_id = request.form.get('employee_id')
    password = request.form.get('password')
    record = db.verify_user_credentials(emp_id)
    if record and hashlib.sha512(password.encode()).digest() == record['PasswordHash']:
        session['user_id'] = emp_id
        session['permissions'] = db.get_user_permissions(emp_id)
        return redirect(url_for('dashboard'))
    flash("Invalid Credentials")
    return redirect(url_for('login'))

@app.route('/dashboard')
def dashboard():
    if 'user_id' not in session: return redirect(url_for('login'))
    return render_template('dashboard.html')

# --- INVENTORY ---
@app.route('/inventory')
def inventory_page():
    if 'user_id' not in session: return redirect(url_for('login'))
    items = db.get_inventory_list()
    return render_template('inventory.html', items=items)

@app.route('/inventory/add', methods=['GET', 'POST'])
def add_item():
    if request.method == 'POST':
        db.add_new_product(request.form['name'], request.form['sku'], request.form['price'], request.form['qty'])
        flash("Product Added")
        return redirect(url_for('inventory_page'))
    return render_template('add_item.html')

@app.route('/inventory/update/<int:inv_id>', methods=['GET', 'POST'])
def update_item(inv_id):
    if request.method == 'POST':
        db.update_stock(inv_id, request.form['qty'])
        flash("Stock Adjusted")
        return redirect(url_for('inventory_page'))
    return render_template('update_item.html', inv_id=inv_id)

@app.route('/inventory/remove/<int:inv_id>')
def remove_item(inv_id):
    db.delete_inventory_item(inv_id)
    flash("Item Removed")
    return redirect(url_for('inventory_page'))

# --- ADMIN ---
@app.route('/admin/users')
def admin_users():
    # Security Check
    if 'user_id' not in session or 'manage_hr' not in session.get('permissions', []):
        flash("You do not have administrative clearance for this module.")
        return redirect(url_for('dashboard'))

    # Fetch users via the database.py function we fixed
    users = db.get_manageable_users(session['user_id'])
    
    # Debug print to ensure data is flowing
    print(f"DEBUG: Staff count found: {len(users)}")
    
    return render_template('admin_users.html', users=users)

@app.route('/admin/edit_user/<int:emp_id>', methods=['GET', 'POST'])
def edit_user(emp_id):
    if request.method == 'POST':
        db.update_employee_job(emp_id, request.form['job_id'])
        flash("User Role Updated")
        return redirect(url_for('admin_users'))
    jobs = db.get_all_jobs()
    return render_template('edit_user.html', emp_id=emp_id, jobs=jobs)

# --- DYNAMIC VIEWS ---
@app.route('/view/<table>')
def view_table(table):
    if 'user_id' not in session: return redirect(url_for('login'))
    cols, rows = db.get_any_table_data(table)
    return render_template('table_view.html', table_name=table, columns=cols, rows=rows)

@app.route('/logout')
def logout():
    session.clear()
    return redirect(url_for('login'))

if __name__ == '__main__':
    app.run(debug=True, port=8080)