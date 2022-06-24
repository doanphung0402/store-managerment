create table if not exists categories
(
    id          bigint unsigned auto_increment
        primary key,
    name        varchar(32)             not null,
    description varchar(255) default '' null
)
    charset = utf8mb4;

create table if not exists products
(
    id            bigint unsigned auto_increment
        primary key,
    name          varchar(64)                              not null,
    category_id   bigint unsigned                          not null,
    brand         varchar(32)    default ''                null,
    description   varchar(255)   default ''                null,
    image_url     varchar(255)   default ''                null,
    created_at    timestamp      default CURRENT_TIMESTAMP null,
    updated_at    timestamp      default CURRENT_TIMESTAMP null,
    weight        decimal(10, 2) default 0.00              not null,
    record_status varchar(32)    default 'Đang hoạt động'  null,
    constraint products_category_id_foreign
        foreign key (category_id) references categories (id)
)
    charset = utf8mb4;

create table if not exists roles
(
    id   bigint unsigned auto_increment
        primary key,
    name varchar(32) not null
)
    charset = utf8mb4;

create table if not exists suppliers
(
    id              bigint unsigned auto_increment
        primary key,
    code            varchar(16)                             not null,
    name            varchar(64)                             not null,
    address         varchar(128)                            not null,
    phone           varchar(11)    default ''               null,
    email           varchar(128)   default ''               null,
    website         varchar(128)   default ''               null,
    description     varchar(255)   default ''               null,
    fax             varchar(32)    default ''               null,
    debt            decimal(12, 2) default 0.00             null,
    activity_status varchar(32)    default 'Đang hợp tác'   null,
    record_status   varchar(32)    default 'Đang hoạt động' null
)
    charset = utf8mb4;

create table if not exists users
(
    id            bigint unsigned auto_increment
        primary key,
    username      varchar(32)  not null,
    password      varchar(255) not null,
    email         varchar(128) not null,
    record_status varchar(32)  null,
    constraint email
        unique (email),
    constraint username
        unique (username)
)
    charset = utf8mb4;

create table if not exists check_sheets
(
    id         bigint unsigned auto_increment
        primary key,
    code       varchar(8)                             not null,
    created_at timestamp    default CURRENT_TIMESTAMP null,
    created_by bigint unsigned                        not null,
    note       varchar(255) default ''                null,
    constraint code
        unique (code),
    constraint check_sheets_created_by_foreign
        foreign key (created_by) references users (id)
)
    charset = utf8mb4;

create table if not exists orders
(
    id                 bigint unsigned auto_increment
        primary key,
    code               varchar(8)                               not null,
    supplier_id        bigint unsigned                          not null,
    total_amount       decimal(12, 2)                           not null,
    paid_amount        decimal(12, 2) default 0.00              null,
    expected_time      date                                     not null,
    transaction_status varchar(32)    default 'Chưa thanh toán' null,
    imported_status    varchar(32)    default 'Chờ nhập kho'    null,
    created_at         timestamp      default CURRENT_TIMESTAMP null,
    updated_at         timestamp      default CURRENT_TIMESTAMP null,
    created_by         bigint unsigned                          not null,
    status             varchar(32)    default 'Đang giao dịch'  null,
    description        varchar(255)   default ''                not null,
    discount           double                                   not null,
    constraint orders_code_unique
        unique (code),
    constraint orders_created_by_foreign
        foreign key (created_by) references users (id),
    constraint orders_supplier_id_foreign
        foreign key (supplier_id) references suppliers (id)
)
    charset = utf8mb4;

create table if not exists import_receipts
(
    id         bigint unsigned auto_increment
        primary key,
    code       varchar(8)                          not null,
    order_id   bigint unsigned                     not null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    created_by bigint unsigned                     not null,
    constraint import_receipts_fk_orders
        foreign key (order_id) references orders (id),
    constraint import_receipts_fk_users
        foreign key (created_by) references users (id)
)
    charset = utf8mb4;

create table if not exists payment_invoice
(
    id         bigint unsigned auto_increment
        primary key,
    amount     decimal(12, 2)                      not null,
    order_id   bigint unsigned                     not null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    created_by bigint unsigned                     not null,
    constraint payment_invoice_fk_orders
        foreign key (order_id) references orders (id),
    constraint payment_invoice_fk_users
        foreign key (created_by) references users (id)
)
    charset = utf8mb4;

create table if not exists return_receipts
(
    id         int unsigned auto_increment
        primary key,
    code       varchar(8)                            null,
    order_id   bigint unsigned                       null,
    note       varchar(32) default ''                null,
    created_at timestamp   default CURRENT_TIMESTAMP null,
    created_by bigint unsigned                       null,
    constraint code_UNIQUE
        unique (code),
    constraint fk_return_receipts_1
        foreign key (order_id) references orders (id)
            on update cascade,
    constraint fk_return_receipts_2
        foreign key (created_by) references users (id)
            on update cascade
)
    charset = utf8mb4;

create index fk_return_receipts_1_idx
    on return_receipts (order_id);

create index fk_return_receipts_2_idx
    on return_receipts (created_by);

create table if not exists user_role
(
    user_id bigint unsigned not null,
    role_id bigint unsigned not null,
    primary key (user_id, role_id),
    constraint user_role_ibfk_1
        foreign key (user_id) references users (id),
    constraint user_role_ibfk_2
        foreign key (role_id) references roles (id)
)
    charset = utf8mb4;

create index role_id
    on user_role (role_id);

create table if not exists variants
(
    id                 bigint unsigned auto_increment
        primary key,
    product_id         bigint unsigned                      not null,
    code               varchar(16)                          not null,
    inventory_quantity bigint                               not null,
    sellable_quantity  bigint                               not null,
    size               varchar(8)  default ''               null,
    color              varchar(16) default ''               null,
    material           varchar(32) default ''               null,
    unit               varchar(16) default ''               null,
    original_price     decimal(12, 2)                       not null,
    whole_sale_price   decimal(12, 2)                       not null,
    retail_price       decimal(12, 2)                       not null,
    record_status      varchar(32) default 'Đang hoạt động' null,
    sell_status        varchar(32) default 'Có thể bán'     null,
    image_url          varchar(255)                         null,
    constraint variants_code_unique
        unique (code),
    constraint variants_products_id_foreign
        foreign key (product_id) references products (id)
)
    charset = utf8mb4;

create index variants_product_id_foreign
    on variants (product_id);

create table if not exists variants_check_sheets
(
    variant_id         bigint unsigned         not null,
    checksheet_id      bigint unsigned         not null,
    inventory_quantity bigint                  not null,
    real_quantity      bigint                  not null,
    note               varchar(255) default '' null,
    primary key (variant_id, checksheet_id),
    constraint variants_check_sheets_ibfk_1
        foreign key (checksheet_id) references check_sheets (id),
    constraint variants_check_sheets_ibfk_2
        foreign key (variant_id) references variants (id)
)
    charset = utf8mb4;

create index checksheet_id
    on variants_check_sheets (checksheet_id);

create table if not exists variants_import_receipts
(
    variant_id        bigint unsigned auto_increment,
    import_receipt_id bigint unsigned not null,
    quantity          bigint          not null,
    primary key (variant_id, import_receipt_id),
    constraint fk_import_receipts
        foreign key (import_receipt_id) references import_receipts (id),
    constraint fk_variants
        foreign key (variant_id) references variants (id)
)
    charset = utf8mb4;

create table if not exists variants_orders
(
    order_id          bigint unsigned             not null,
    variant_id        bigint unsigned             not null,
    supplied_quantity bigint                      not null,
    price             decimal(12, 2) default 0.00 null,
    primary key (order_id, variant_id),
    constraint variants_orders_ibfk_1
        foreign key (order_id) references orders (id),
    constraint variants_orders_ibfk_2
        foreign key (variant_id) references variants (id)
)
    charset = utf8mb4;

create index variant_id
    on variants_orders (variant_id);

create table if not exists variants_return_receipts
(
    variant_id        bigint unsigned             not null,
    return_receipt_id int unsigned                not null,
    quantity          bigint         default 0    null,
    amount_each       decimal(12, 2) default 0.00 null,
    primary key (variant_id, return_receipt_id),
    constraint fk_variants_return_receipts_1
        foreign key (variant_id) references variants (id)
            on update cascade,
    constraint fk_variants_return_receipts_2
        foreign key (return_receipt_id) references return_receipts (id)
            on update cascade
)
    charset = utf8mb4;

create index fk_variants_return_receipts_2_idx
    on variants_return_receipts (return_receipt_id);


