 create table answer (
        id bigint generated by default as identity,
        answered_by_id varchar(255),
        form_id bigint not null,
        primary key (id)
    )

 create table answer_details (
        answer_id bigint not null,
        response varchar(255),
        question_id bigint not null,
        primary key (answer_id, question_id)
    )

create table form (
        id bigint generated by default as identity,
        access_password varchar(255),
        access_username varchar(255) array,
        created_at timestamp(6) not null,
        created_by varchar(255),
        description varchar(255),
        form_has_login varchar(255) check (form_has_login in ('PRIVATE','PASSWORD','PUBLIC')),
        id_public varchar(255),
        is_published boolean not null,
        link varchar(255),
        responses_count integer,
        send_email_responses_count boolean not null,
        title varchar(255) not null,
        views integer,
        primary key (id)
    )

create table question (
        id bigint generated by default as identity,
        placeholder varchar(255),
        question_description varchar(255),
        required boolean not null,
        title varchar(255) not null,
        type varchar(255) not null,
        form_id bigint not null,
        primary key (id)
    )

 create table question_options (
        question_id bigint not null,
        option varchar(255)
    )

 create table users (
        id varchar(255) not null,
        login varchar(255) not null,
        password varchar(255),
        reset_token varchar(255),
        role varchar(255) check (role in ('ADMIN','USER')),
        verification_token varchar(255),
        verified boolean not null,
        primary key (id)
    )

