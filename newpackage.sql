CREATE OR REPLACE PACKAGE PROJECT2 AS

type cursor_out is ref cursor;

Procedure show_employees(cursor_ref OUT cursor_out );
Procedure show_customers(cursor_ref OUT cursor_out );
Procedure show_products(cursor_ref OUT cursor_out );
Procedure show_prod_discnt(cursor_ref OUT cursor_out );
Procedure show_purchases(cursor_ref OUT cursor_out );
Procedure show_Logs(cursor_ref OUT cursor_out );
procedure monthly_sale_activities
    (employee_id in employees.eid%type,
    cursor_ref out cursor_out);
procedure add_employee
    (e_id in employees.eid%type,
    e_name in employees.name%type,
    e_telephone in employees.telephone#%type,
    e_email in employees.email%type);
procedure add_purchase
    (e_id in purchases.eid%type,
    p_id in purchases.pid%type,
    c_id in purchases.cid%type,
    pur_qty in purchases.quantity%type,
    pur_unit_price in purchases.unit_price%type);

END PROJECT2;
/
show errors

CREATE OR REPLACE PACKAGE BODY PROJECT2 AS

-- Question2
procedure show_employees(cursor_ref OUT cursor_out) is
begin
open cursor_ref for 
select * from employees order by eid asc;
end;

procedure show_customers(cursor_ref OUT cursor_out) is
begin
open cursor_ref for 
select * from customers order by cid asc;
end;

procedure show_products(cursor_ref OUT cursor_out) is
begin
open cursor_ref for 
select * from products order by pid asc;
end;

procedure show_prod_discnt(cursor_ref OUT cursor_out) is
begin
open cursor_ref for 
select * from prod_discnt order by discnt_category asc;
end;

procedure show_purchases(cursor_ref OUT cursor_out) is
begin
open cursor_ref for 
select * from purchases order by pur# asc;
end;

procedure show_logs(cursor_ref OUT cursor_out) is
begin
open cursor_ref for 
select * from logs order by log# asc;
end;

--Question3
procedure monthly_sale_activities
(employee_id in employees.eid%type, cursor_ref out cursor_out) is
eid_check number;
no_eid exception;
begin

    select count(*) into eid_check from employees where eid=employee_id;

    if eid_check=0 then
        raise no_eid;
    end if;

    open cursor_ref for
    select e.eid,name,
            to_char(pur_time, 'MON') as Month,to_char(pur_time, 'YYYY') as Year,
            count(pu.eid) as no_of_sales,sum(quantity) as qty,sum(payment) as total
    from employees e, purchases pu where e.eid = employee_id and e.eid = pu.eid
    group by e.eid, name, to_char(pur_time, 'MON'), to_char(pur_time, 'YYYY');
    
exception
    when no_eid then
        raise_application_error(-20001, 'eid not in DB');
    when no_data_found then
        raise_application_error(-20002, 'no data');
    when others then
        raise_application_error(-20003, 'SQL Exception');

end;

--Question4
    
procedure add_employee
(e_id in employees.eid%type,e_name in employees.name%type,
e_telephone in employees.telephone#%type,e_email in employees.email%type) is

begin
    
    insert into employees values(e_id, e_name, e_telephone, e_email);

exception
    when dup_val_on_index then
        raise_application_error(-20004, 'duplicate issue');
    when others then
        raise_application_error(-20003, 'SQL Exception');
    
end;

--Question5
procedure add_purchase
(e_id in purchases.eid%type,
p_id in purchases.pid%type,
c_id in purchases.cid%type,
pur_qty in purchases.quantity%type,
pur_unit_price in purchases.unit_price%type) is

eid_check number;
no_eid exception;
pid_check number;
no_pid exception;
cid_check number;
no_cid exception;
no_qty exception;
check_quantity exception;
payment_val purchases.payment%type;
qoh_val products.qoh%type;
saving_val purchases.saving%type;
precord products%rowtype;
begin
    select count(*) into eid_check from employees where eid=e_id;
    select count(*) into pid_check from products where pid=p_id;
    select count(*) into cid_check from customers where cid=c_id;
    select * into precord from products where pid=p_id;
    if eid_check = 0 then
        raise no_eid;
    end if;
    if pid_check = 0 then
        raise no_pid;
    end if;
    if cid_check = 0 then
        raise no_cid;
    end if;
    if pur_qty < 1 then
        raise check_quantity;
    end if;
    
    payment_val := pur_unit_price * pur_qty;
    saving_val := precord.orig_price*pur_qty-payment_val;
    qoh_val := precord.qoh;

    if(qoh_val < pur_qty) then
        raise no_qty;
    end if;
        
    insert into purchases values(seqpur#.nextval, e_id, p_id, c_id, sysdate, pur_qty, pur_unit_price, payment_val, saving_val);

exception
    when no_eid then
        raise_application_error(-20001, 'eid not in DB');
    when no_pid then
        raise_application_error(-20005, 'pid not in DB');
    when no_cid then
        raise_application_error(-20006, 'cid not in DB');
    when check_quantity then
        raise_application_error(-20007, 'check the eneterd quantity');
    when no_qty then
        raise_application_error(-20008, 'Insufficient quantity in stock.');
    when dup_val_on_index then
        raise_application_error(-20004, 'duplicate issue');
    when no_data_found then
        raise_application_error(-20002, 'no data');
    when others then
        raise_application_error(-20003, 'SQL Exception');
end;

END PROJECT2;
/
show errors
  