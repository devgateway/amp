--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: donors; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE donors (
    id integer NOT NULL,
    code character varying(255),
    currency character varying(255),
    cofunding_only boolean,
    institutions_responsible_for_oda text,
    total_staff_in_country integer,
    total_expatriate_staff integer,
    total_local_staff integer,
    officer_responsible character varying(255),
    field_office_address text,
    field_office_phone character varying(255),
    field_office_email character varying(255),
    field_office_website character varying(255),
    head_of_mission_name character varying(255),
    head_of_mission_email character varying(255),
    head_of_cooperation_name character varying(255),
    head_of_cooperation_email character varying(255),
    first_focal_point_name character varying(255),
    first_focal_point_email character varying(255),
    second_focal_point_name character varying(255),
    second_focal_point_email character varying(255),
    flag_file_name character varying(255),
    flag_content_type character varying(255),
    flag_file_size integer,
    flag_updated_at timestamp without time zone,
    profile_picture_file_name character varying(255),
    profile_picture_content_type character varying(255),
    profile_picture_file_size integer,
    profile_picture_updated_at timestamp without time zone
);


--
-- Name: exchange_rates; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE exchange_rates (
    id integer NOT NULL,
    year integer,
    currency character varying(255),
    euro_rate double precision
);


--
-- Name: funding_forecasts; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE funding_forecasts (
    id integer NOT NULL,
    project_id integer,
    year integer,
    payments integer,
    commitments integer,
    on_budget boolean DEFAULT false,
    on_treasury boolean DEFAULT false
);


--
-- Name: projects; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE projects (
    id integer NOT NULL,
    title text,
    description text,
    donor_project_number character varying(255),
    oecd_number integer,
    recipient_country_budget_nr character varying(255),
    recipient_code integer,
    region_code integer,
    income_code character varying(255),
    start date,
    "end" date,
    comments text,
    website character varying(255),
    grant_loan integer,
    national_regional integer,
    type_of_implementation integer,
    government_counterpart character varying(255),
    prj_status integer,
    data_status integer DEFAULT 0,
    input_state character varying(255),
    donor_id integer,
    donor_agency_id integer,
    dac_sector_id integer,
    crs_sector_id integer,
    type_of_aid_id integer,
    country_strategy_id integer,
    government_counterpart_id integer,
    gender_policy_marker integer,
    environment_policy_marker integer,
    biodiversity_marker integer,
    climate_change_marker integer,
    desertification_marker integer,
    officer_responsible_name character varying(255),
    officer_responsible_phone character varying(255),
    officer_responsible_email character varying(255),
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: accessible_forecasts; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW accessible_forecasts AS
    SELECT donors.id AS donor_id, projects.id AS project_id, projects.data_status, exchange_rates.currency, funding_forecasts.year, round(((funding_forecasts.payments)::double precision * (exchange_rates.euro_rate / local_rates.euro_rate))) AS payments, round(((funding_forecasts.commitments)::double precision * (exchange_rates.euro_rate / local_rates.euro_rate))) AS commitments FROM ((((funding_forecasts JOIN projects ON ((funding_forecasts.project_id = projects.id))) JOIN donors ON ((projects.donor_id = donors.id))) JOIN exchange_rates ON ((exchange_rates.year = funding_forecasts.year))) JOIN exchange_rates local_rates ON (((local_rates.year = funding_forecasts.year) AND ((local_rates.currency)::text = (donors.currency)::text))));


--
-- Name: fundings; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE fundings (
    id integer NOT NULL,
    project_id integer,
    year integer,
    payments_q1 integer,
    payments_q2 integer,
    payments_q3 integer,
    payments_q4 integer,
    commitments integer,
    on_budget boolean DEFAULT false,
    on_treasury boolean DEFAULT false
);


--
-- Name: accessible_fundings; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW accessible_fundings AS
    SELECT donors.id AS donor_id, projects.id AS project_id, projects.data_status, exchange_rates.currency, fundings.year, round(((fundings.payments_q1)::double precision * (exchange_rates.euro_rate / local_rates.euro_rate))) AS payments_q1, round(((fundings.payments_q2)::double precision * (exchange_rates.euro_rate / local_rates.euro_rate))) AS payments_q2, round(((fundings.payments_q3)::double precision * (exchange_rates.euro_rate / local_rates.euro_rate))) AS payments_q3, round(((fundings.payments_q4)::double precision * (exchange_rates.euro_rate / local_rates.euro_rate))) AS payments_q4, round(((fundings.commitments)::double precision * (exchange_rates.euro_rate / local_rates.euro_rate))) AS commitments FROM ((((fundings JOIN projects ON ((fundings.project_id = projects.id))) JOIN donors ON ((projects.donor_id = donors.id))) JOIN exchange_rates ON ((exchange_rates.year = fundings.year))) JOIN exchange_rates local_rates ON (((local_rates.year = fundings.year) AND ((local_rates.currency)::text = (donors.currency)::text))));


--
-- Name: agencies; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE agencies (
    id integer NOT NULL,
    name character varying(255),
    type character varying(255),
    contact_name character varying(255),
    contact_phone character varying(255),
    contact_email character varying(255)
);


--
-- Name: cofundings; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE cofundings (
    id integer NOT NULL,
    project_id integer,
    donor_id integer,
    amount integer,
    currency character varying(255)
);


--
-- Name: contracted_agencies_projects; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE contracted_agencies_projects (
    project_id integer,
    contracted_agency_id integer
);


--
-- Name: country_strategies; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE country_strategies (
    id integer NOT NULL,
    website character varying(255),
    comment text,
    strategy_paper boolean,
    start date,
    "end" date,
    total_amount_foreseen integer,
    programming_responsibility integer,
    project_appraisal_responsibility integer,
    tenders_responsibility integer,
    commitments_and_payments_responsibility integer,
    monitoring_and_evaluation_responsibility integer,
    commitment_to_budget_support integer,
    commitment_to_sectorwide_approaches_and_common_funds integer,
    commitment_to_projects integer,
    donor_id integer
);


--
-- Name: country_strategy_translations; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE country_strategy_translations (
    id integer NOT NULL,
    locale character varying(255),
    strategy_number character varying(255),
    description text,
    country_strategy_id integer
);


--
-- Name: crs_sector_translations; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE crs_sector_translations (
    id integer NOT NULL,
    locale character varying(255),
    name character varying(255),
    description text,
    crs_sector_id integer
);


--
-- Name: crs_sectors; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE crs_sectors (
    id integer NOT NULL,
    code integer,
    dac_sector_id integer
);


--
-- Name: dac_sector_translations; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE dac_sector_translations (
    id integer NOT NULL,
    locale character varying(255),
    name character varying(255),
    description text,
    dac_sector_id integer
);


--
-- Name: dac_sectors; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE dac_sectors (
    id integer NOT NULL,
    code integer
);


--
-- Name: districts; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE districts (
    id integer NOT NULL,
    name character varying(255),
    code character varying(255),
    province_id integer
);


--
-- Name: donor_agencies; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE donor_agencies (
    id integer NOT NULL,
    name character varying(255),
    code character varying(255),
    acronym character varying(255),
    donor_id integer
);


--
-- Name: donor_translations; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE donor_translations (
    id integer NOT NULL,
    locale character varying(255),
    name character varying(255),
    donor_id integer
);


--
-- Name: geo_relevances; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE geo_relevances (
    id integer NOT NULL,
    project_id integer,
    province_id integer,
    district_id integer
);


--
-- Name: glossaries; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE glossaries (
    id integer NOT NULL,
    model character varying(255),
    method character varying(255),
    locale character varying(255),
    description text
);


--
-- Name: historic_fundings; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE historic_fundings (
    id integer NOT NULL,
    project_id integer,
    payments integer,
    commitments integer
);


--
-- Name: implementing_agencies_projects; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE implementing_agencies_projects (
    project_id integer,
    implementing_agency_id integer
);


--
-- Name: mdg_relevances; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE mdg_relevances (
    id integer NOT NULL,
    project_id integer,
    mdg_id integer,
    target_id integer
);


--
-- Name: mdg_translations; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE mdg_translations (
    id integer NOT NULL,
    locale character varying(255),
    name character varying(255),
    description text,
    mdg_id integer
);


--
-- Name: mdgs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE mdgs (
    id integer NOT NULL
);


--
-- Name: plugin_schema_info; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE plugin_schema_info (
    plugin_name character varying(255),
    version integer
);


--
-- Name: provinces; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE provinces (
    id integer NOT NULL,
    name character varying(255),
    code character varying(255)
);


--
-- Name: provinces_sector_details; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE provinces_sector_details (
    province_id integer,
    sector_detail_id integer
);


--
-- Name: roles; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE roles (
    id integer NOT NULL,
    title character varying(255),
    description text,
    layout character varying(255)
);


--
-- Name: schema_migrations; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE schema_migrations (
    version character varying(255) NOT NULL
);


--
-- Name: sector_amounts; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE sector_amounts (
    id integer NOT NULL,
    amount integer,
    country_strategy_id integer,
    focal_sector_id integer
);


--
-- Name: sector_details; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE sector_details (
    id integer NOT NULL,
    amount integer,
    country_strategy_id integer,
    focal_sector_id integer,
    focal_sector_type character varying(255)
);


--
-- Name: settings; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE settings (
    key character varying(255),
    value text
);


--
-- Name: target_translations; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE target_translations (
    id integer NOT NULL,
    locale character varying(255),
    name text,
    target_id integer
);


--
-- Name: targets; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE targets (
    id integer NOT NULL,
    mdg_id integer
);


--
-- Name: total_odas; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE total_odas (
    id integer NOT NULL,
    commitments integer,
    year integer,
    disbursements integer,
    country_strategy_id integer
);


--
-- Name: type_of_aid_translations; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE type_of_aid_translations (
    id integer NOT NULL,
    locale character varying(255),
    name character varying(255),
    type_of_aid_id integer
);


--
-- Name: types_of_aid; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE types_of_aid (
    id integer NOT NULL
);


--
-- Name: users; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE users (
    id integer NOT NULL,
    name character varying(100) DEFAULT ''::character varying,
    email character varying(100),
    role_id integer,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    crypted_password character varying(40),
    salt character varying(40),
    remember_token character varying(40),
    remember_token_expires_at timestamp without time zone,
    donor_id integer
);


--
-- Name: agencies_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE agencies_id_seq
    START WITH 378066745
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: agencies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE agencies_id_seq OWNED BY agencies.id;


--
-- Name: cofundings_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cofundings_id_seq
    START WITH 48
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cofundings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE cofundings_id_seq OWNED BY cofundings.id;


--
-- Name: country_strategies_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE country_strategies_id_seq
    START WITH 698286514
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: country_strategies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE country_strategies_id_seq OWNED BY country_strategies.id;


--
-- Name: country_strategy_translations_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE country_strategy_translations_id_seq
    START WITH 25
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: country_strategy_translations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE country_strategy_translations_id_seq OWNED BY country_strategy_translations.id;


--
-- Name: crs_sector_translations_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE crs_sector_translations_id_seq
    START WITH 391
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: crs_sector_translations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE crs_sector_translations_id_seq OWNED BY crs_sector_translations.id;


--
-- Name: crs_sectors_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE crs_sectors_id_seq
    START WITH 1170
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: crs_sectors_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE crs_sectors_id_seq OWNED BY crs_sectors.id;


--
-- Name: dac_sector_translations_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE dac_sector_translations_id_seq
    START WITH 77
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: dac_sector_translations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE dac_sector_translations_id_seq OWNED BY dac_sector_translations.id;


--
-- Name: dac_sectors_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE dac_sectors_id_seq
    START WITH 255
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: dac_sectors_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE dac_sectors_id_seq OWNED BY dac_sectors.id;


--
-- Name: districts_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE districts_id_seq
    START WITH 784320005
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: districts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE districts_id_seq OWNED BY districts.id;


--
-- Name: donor_agencies_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE donor_agencies_id_seq
    START WITH 20952778
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: donor_agencies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE donor_agencies_id_seq OWNED BY donor_agencies.id;


--
-- Name: donor_translations_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE donor_translations_id_seq
    START WITH 37
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: donor_translations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE donor_translations_id_seq OWNED BY donor_translations.id;


--
-- Name: donors_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE donors_id_seq
    START WITH 18219206
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: donors_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE donors_id_seq OWNED BY donors.id;


--
-- Name: exchange_rates_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE exchange_rates_id_seq
    START WITH 26
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: exchange_rates_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE exchange_rates_id_seq OWNED BY exchange_rates.id;


--
-- Name: funding_forecasts_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE funding_forecasts_id_seq
    START WITH 395
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: funding_forecasts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE funding_forecasts_id_seq OWNED BY funding_forecasts.id;


--
-- Name: fundings_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE fundings_id_seq
    START WITH 387
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: fundings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE fundings_id_seq OWNED BY fundings.id;


--
-- Name: geo_relevances_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE geo_relevances_id_seq
    START WITH 469
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: geo_relevances_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE geo_relevances_id_seq OWNED BY geo_relevances.id;


--
-- Name: glossaries_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE glossaries_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: glossaries_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE glossaries_id_seq OWNED BY glossaries.id;


--
-- Name: historic_fundings_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE historic_fundings_id_seq
    START WITH 283
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: historic_fundings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE historic_fundings_id_seq OWNED BY historic_fundings.id;


--
-- Name: mdg_relevances_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE mdg_relevances_id_seq
    START WITH 645
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: mdg_relevances_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE mdg_relevances_id_seq OWNED BY mdg_relevances.id;


--
-- Name: mdg_translations_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE mdg_translations_id_seq
    START WITH 19
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: mdg_translations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE mdg_translations_id_seq OWNED BY mdg_translations.id;


--
-- Name: mdgs_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE mdgs_id_seq
    START WITH 10
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: mdgs_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE mdgs_id_seq OWNED BY mdgs.id;


--
-- Name: projects_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE projects_id_seq
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: projects_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE projects_id_seq OWNED BY projects.id;


--
-- Name: provinces_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE provinces_id_seq
    START WITH 995756884
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: provinces_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE provinces_id_seq OWNED BY provinces.id;


--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE roles_id_seq
    START WITH 935545107
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE roles_id_seq OWNED BY roles.id;


--
-- Name: sector_amounts_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sector_amounts_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: sector_amounts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE sector_amounts_id_seq OWNED BY sector_amounts.id;


--
-- Name: sector_details_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sector_details_id_seq
    START WITH 493
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: sector_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE sector_details_id_seq OWNED BY sector_details.id;


--
-- Name: target_translations_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE target_translations_id_seq
    START WITH 39
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: target_translations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE target_translations_id_seq OWNED BY target_translations.id;


--
-- Name: targets_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE targets_id_seq
    START WITH 20
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: targets_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE targets_id_seq OWNED BY targets.id;


--
-- Name: total_odas_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE total_odas_id_seq
    START WITH 759883596
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: total_odas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE total_odas_id_seq OWNED BY total_odas.id;


--
-- Name: type_of_aid_translations_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE type_of_aid_translations_id_seq
    START WITH 25
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: type_of_aid_translations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE type_of_aid_translations_id_seq OWNED BY type_of_aid_translations.id;


--
-- Name: types_of_aid_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE types_of_aid_id_seq
    START WITH 13
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: types_of_aid_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE types_of_aid_id_seq OWNED BY types_of_aid.id;


--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE users_id_seq
    START WITH 541702177
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE agencies ALTER COLUMN id SET DEFAULT nextval('agencies_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE cofundings ALTER COLUMN id SET DEFAULT nextval('cofundings_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE country_strategies ALTER COLUMN id SET DEFAULT nextval('country_strategies_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE country_strategy_translations ALTER COLUMN id SET DEFAULT nextval('country_strategy_translations_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE crs_sector_translations ALTER COLUMN id SET DEFAULT nextval('crs_sector_translations_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE crs_sectors ALTER COLUMN id SET DEFAULT nextval('crs_sectors_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE dac_sector_translations ALTER COLUMN id SET DEFAULT nextval('dac_sector_translations_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE dac_sectors ALTER COLUMN id SET DEFAULT nextval('dac_sectors_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE districts ALTER COLUMN id SET DEFAULT nextval('districts_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE donor_agencies ALTER COLUMN id SET DEFAULT nextval('donor_agencies_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE donor_translations ALTER COLUMN id SET DEFAULT nextval('donor_translations_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE donors ALTER COLUMN id SET DEFAULT nextval('donors_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE exchange_rates ALTER COLUMN id SET DEFAULT nextval('exchange_rates_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE funding_forecasts ALTER COLUMN id SET DEFAULT nextval('funding_forecasts_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE fundings ALTER COLUMN id SET DEFAULT nextval('fundings_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE geo_relevances ALTER COLUMN id SET DEFAULT nextval('geo_relevances_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE glossaries ALTER COLUMN id SET DEFAULT nextval('glossaries_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE historic_fundings ALTER COLUMN id SET DEFAULT nextval('historic_fundings_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE mdg_relevances ALTER COLUMN id SET DEFAULT nextval('mdg_relevances_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE mdg_translations ALTER COLUMN id SET DEFAULT nextval('mdg_translations_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE mdgs ALTER COLUMN id SET DEFAULT nextval('mdgs_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE projects ALTER COLUMN id SET DEFAULT nextval('projects_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE provinces ALTER COLUMN id SET DEFAULT nextval('provinces_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE roles ALTER COLUMN id SET DEFAULT nextval('roles_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE sector_amounts ALTER COLUMN id SET DEFAULT nextval('sector_amounts_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE sector_details ALTER COLUMN id SET DEFAULT nextval('sector_details_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE target_translations ALTER COLUMN id SET DEFAULT nextval('target_translations_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE targets ALTER COLUMN id SET DEFAULT nextval('targets_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE total_odas ALTER COLUMN id SET DEFAULT nextval('total_odas_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE type_of_aid_translations ALTER COLUMN id SET DEFAULT nextval('type_of_aid_translations_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE types_of_aid ALTER COLUMN id SET DEFAULT nextval('types_of_aid_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Name: agencies_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY agencies
    ADD CONSTRAINT agencies_pkey PRIMARY KEY (id);


--
-- Name: cofundings_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY cofundings
    ADD CONSTRAINT cofundings_pkey PRIMARY KEY (id);


--
-- Name: country_strategies_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY country_strategies
    ADD CONSTRAINT country_strategies_pkey PRIMARY KEY (id);


--
-- Name: country_strategy_translations_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY country_strategy_translations
    ADD CONSTRAINT country_strategy_translations_pkey PRIMARY KEY (id);


--
-- Name: crs_sector_translations_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY crs_sector_translations
    ADD CONSTRAINT crs_sector_translations_pkey PRIMARY KEY (id);


--
-- Name: crs_sectors_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY crs_sectors
    ADD CONSTRAINT crs_sectors_pkey PRIMARY KEY (id);


--
-- Name: dac_sector_translations_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY dac_sector_translations
    ADD CONSTRAINT dac_sector_translations_pkey PRIMARY KEY (id);


--
-- Name: dac_sectors_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY dac_sectors
    ADD CONSTRAINT dac_sectors_pkey PRIMARY KEY (id);


--
-- Name: districts_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY districts
    ADD CONSTRAINT districts_pkey PRIMARY KEY (id);


--
-- Name: donor_agencies_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY donor_agencies
    ADD CONSTRAINT donor_agencies_pkey PRIMARY KEY (id);


--
-- Name: donor_translations_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY donor_translations
    ADD CONSTRAINT donor_translations_pkey PRIMARY KEY (id);


--
-- Name: donors_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY donors
    ADD CONSTRAINT donors_pkey PRIMARY KEY (id);


--
-- Name: exchange_rates_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY exchange_rates
    ADD CONSTRAINT exchange_rates_pkey PRIMARY KEY (id);


--
-- Name: funding_forecasts_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY funding_forecasts
    ADD CONSTRAINT funding_forecasts_pkey PRIMARY KEY (id);


--
-- Name: fundings_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY fundings
    ADD CONSTRAINT fundings_pkey PRIMARY KEY (id);


--
-- Name: geo_relevances_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY geo_relevances
    ADD CONSTRAINT geo_relevances_pkey PRIMARY KEY (id);


--
-- Name: glossaries_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY glossaries
    ADD CONSTRAINT glossaries_pkey PRIMARY KEY (id);


--
-- Name: historic_fundings_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY historic_fundings
    ADD CONSTRAINT historic_fundings_pkey PRIMARY KEY (id);


--
-- Name: mdg_relevances_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY mdg_relevances
    ADD CONSTRAINT mdg_relevances_pkey PRIMARY KEY (id);


--
-- Name: mdg_translations_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY mdg_translations
    ADD CONSTRAINT mdg_translations_pkey PRIMARY KEY (id);


--
-- Name: mdgs_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY mdgs
    ADD CONSTRAINT mdgs_pkey PRIMARY KEY (id);


--
-- Name: projects_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY projects
    ADD CONSTRAINT projects_pkey PRIMARY KEY (id);


--
-- Name: provinces_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY provinces
    ADD CONSTRAINT provinces_pkey PRIMARY KEY (id);


--
-- Name: roles_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: sector_amounts_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sector_amounts
    ADD CONSTRAINT sector_amounts_pkey PRIMARY KEY (id);


--
-- Name: sector_details_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sector_details
    ADD CONSTRAINT sector_details_pkey PRIMARY KEY (id);


--
-- Name: target_translations_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY target_translations
    ADD CONSTRAINT target_translations_pkey PRIMARY KEY (id);


--
-- Name: targets_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY targets
    ADD CONSTRAINT targets_pkey PRIMARY KEY (id);


--
-- Name: total_odas_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY total_odas
    ADD CONSTRAINT total_odas_pkey PRIMARY KEY (id);


--
-- Name: type_of_aid_translations_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY type_of_aid_translations
    ADD CONSTRAINT type_of_aid_translations_pkey PRIMARY KEY (id);


--
-- Name: types_of_aid_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY types_of_aid
    ADD CONSTRAINT types_of_aid_pkey PRIMARY KEY (id);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: index_cofundings_on_project_id_and_donor_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_cofundings_on_project_id_and_donor_id ON cofundings USING btree (project_id, donor_id);


--
-- Name: index_crs_sectors_on_dac_sector_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_crs_sectors_on_dac_sector_id ON crs_sectors USING btree (dac_sector_id);


--
-- Name: index_funding_forecasts_on_project_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_funding_forecasts_on_project_id ON funding_forecasts USING btree (project_id);


--
-- Name: index_fundings_on_project_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_fundings_on_project_id ON fundings USING btree (project_id);


--
-- Name: index_glossaries_on_model_and_method_and_locale; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_glossaries_on_model_and_method_and_locale ON glossaries USING btree (model, method, locale);


--
-- Name: index_historic_fundings_on_project_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_historic_fundings_on_project_id ON historic_fundings USING btree (project_id);


--
-- Name: index_users_on_email; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE UNIQUE INDEX index_users_on_email ON users USING btree (email);


--
-- Name: unique_schema_migrations; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE UNIQUE INDEX unique_schema_migrations ON schema_migrations USING btree (version);


--
-- PostgreSQL database dump complete
--

INSERT INTO schema_migrations (version) VALUES ('20081018102204');

INSERT INTO schema_migrations (version) VALUES ('20081018102522');

INSERT INTO schema_migrations (version) VALUES ('20081018102529');

INSERT INTO schema_migrations (version) VALUES ('20081018102539');

INSERT INTO schema_migrations (version) VALUES ('20081018102552');

INSERT INTO schema_migrations (version) VALUES ('20081018102558');

INSERT INTO schema_migrations (version) VALUES ('20081018102607');

INSERT INTO schema_migrations (version) VALUES ('20081018102628');

INSERT INTO schema_migrations (version) VALUES ('20081018105012');

INSERT INTO schema_migrations (version) VALUES ('20081024162540');

INSERT INTO schema_migrations (version) VALUES ('20081025061719');

INSERT INTO schema_migrations (version) VALUES ('20081103185836');

INSERT INTO schema_migrations (version) VALUES ('20081109232115');

INSERT INTO schema_migrations (version) VALUES ('20081123111827');