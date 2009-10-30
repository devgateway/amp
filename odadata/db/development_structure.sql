--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

--
-- Name: plpgsql; Type: PROCEDURAL LANGUAGE; Schema: -; Owner: -
--

-- CREATE PROCEDURAL LANGUAGE plpgsql;


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: donors; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE donors (
    id integer NOT NULL,
    name character varying(255),
    name_es character varying(255),
    code character varying(255),
    currency character varying(255),
    cofunding_only boolean,
    institutions_responsible_for_oda text,
    total_staff_in_country integer,
    total_expatriate_staff integer,
    total_local_staff integer,
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
    profile_picture_updated_at timestamp without time zone,
    primary_group_id character varying(255),
    bluebook_donor boolean
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
    currency character varying(255),
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
    aid_modality_id integer,
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
    updated_at timestamp without time zone,
    delegated_cooperation_id integer,
    delegating_agency_id integer
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
    currency character varying(255),
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
    contact_name character varying(255),
    contact_phone character varying(255),
    contact_email character varying(255)
);


--
-- Name: agencies_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE agencies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: agencies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE agencies_id_seq OWNED BY agencies.id;


--
-- Name: aid_modalities; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE aid_modalities (
    id integer NOT NULL,
    name character varying(255),
    name_es character varying(255),
    group_name character varying(255),
    group_name_es character varying(255)
);


--
-- Name: cofundings; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE cofundings (
    id integer NOT NULL,
    project_id integer,
    donor_id integer,
    amount integer,
    currency character varying(255),
    agency_id integer
);


--
-- Name: cofundings_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cofundings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: cofundings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE cofundings_id_seq OWNED BY cofundings.id;


--
-- Name: complex_reports; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE complex_reports (
    id integer NOT NULL,
    user_id integer,
    data bytea,
    comments text,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


--
-- Name: complex_reports_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE complex_reports_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: complex_reports_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE complex_reports_id_seq OWNED BY complex_reports.id;


--
-- Name: contracted_agencies_projects; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE contracted_agencies_projects (
    project_id integer,
    agency_id integer
);


--
-- Name: country_strategies; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE country_strategies (
    id integer NOT NULL,
    strategy_number character varying(255),
    strategy_number_es character varying(255),
    description text,
    description_es text,
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
    donor_id integer,
    applies_to_bluebook boolean
);


--
-- Name: country_strategies_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE country_strategies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: country_strategies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE country_strategies_id_seq OWNED BY country_strategies.id;


--
-- Name: crs_sectors; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE crs_sectors (
    id integer NOT NULL,
    name character varying(255),
    name_es character varying(255),
    description text,
    description_es text,
    code integer,
    dac_sector_id integer
);


--
-- Name: crs_sectors_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE crs_sectors_id_seq
    START WITH 1185
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: crs_sectors_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE crs_sectors_id_seq OWNED BY crs_sectors.id;


--
-- Name: dac_sectors; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE dac_sectors (
    id integer NOT NULL,
    name character varying(255),
    name_es character varying(255),
    description text,
    description_es text,
    code integer
);


--
-- Name: dac_sectors_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE dac_sectors_id_seq
    START WITH 261
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: dac_sectors_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE dac_sectors_id_seq OWNED BY dac_sectors.id;


--
-- Name: delegated_cooperations; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE delegated_cooperations (
    id integer NOT NULL,
    project_id integer,
    delegating_donor_id integer,
    delegating_agency_id integer
);


--
-- Name: delegated_cooperations_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE delegated_cooperations_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: delegated_cooperations_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE delegated_cooperations_id_seq OWNED BY delegated_cooperations.id;


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
-- Name: districts_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE districts_id_seq
    START WITH 651
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: districts_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE districts_id_seq OWNED BY districts.id;


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
-- Name: donor_agencies_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE donor_agencies_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: donor_agencies_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE donor_agencies_id_seq OWNED BY donor_agencies.id;


--
-- Name: donor_details; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE donor_details (
    id integer NOT NULL,
    total_staff_in_country integer,
    total_expatriate_staff integer,
    total_local_staff integer,
    year integer,
    donor_id integer
);


--
-- Name: donor_details_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE donor_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: donor_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE donor_details_id_seq OWNED BY donor_details.id;


--
-- Name: donor_groups; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE donor_groups (
    key character varying(255) NOT NULL,
    name character varying(255),
    name_es character varying(255)
);


--
-- Name: donors_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE donors_id_seq
    START WITH 1
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
    START WITH 1
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
    START WITH 1
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
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: fundings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE fundings_id_seq OWNED BY fundings.id;


--
-- Name: geo_relevances; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE geo_relevances (
    id integer NOT NULL,
    project_id integer,
    province_id integer,
    district_id integer,
    amount double precision
);


--
-- Name: geo_relevances_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE geo_relevances_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: geo_relevances_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE geo_relevances_id_seq OWNED BY geo_relevances.id;


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
-- Name: historic_fundings; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE historic_fundings (
    id integer NOT NULL,
    project_id integer,
    currency character varying(255),
    payments integer,
    commitments integer
);


--
-- Name: historic_fundings_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE historic_fundings_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: historic_fundings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE historic_fundings_id_seq OWNED BY historic_fundings.id;


--
-- Name: implementing_agencies_projects; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE implementing_agencies_projects (
    project_id integer,
    agency_id integer
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
-- Name: mdg_relevances_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE mdg_relevances_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: mdg_relevances_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE mdg_relevances_id_seq OWNED BY mdg_relevances.id;


--
-- Name: mdgs; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE mdgs (
    id integer NOT NULL,
    name character varying(255),
    name_es character varying(255),
    description text,
    description_es text
);


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
-- Name: plugin_schema_migrations; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE plugin_schema_migrations (
    plugin_name character varying(255),
    version character varying(255)
);


--
-- Name: sector_relevances; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE sector_relevances (
    id integer NOT NULL,
    project_id integer,
    dac_sector_id integer,
    crs_sector_id integer,
    amount double precision
);


--
-- Name: project_dac_sector_shares; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW project_dac_sector_shares AS
    SELECT sum(sector_relevances.amount) AS amount, projects.id AS project_id, sector_relevances.dac_sector_id FROM projects, sector_relevances WHERE (sector_relevances.project_id = projects.id) GROUP BY projects.id, sector_relevances.dac_sector_id;


--
-- Name: project_payment_totals; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW project_payment_totals AS
    SELECT projects.id AS project_id, projects.donor_id, projects.data_status, accessible_fundings.year, accessible_fundings.currency, sum((((accessible_fundings.payments_q1 + accessible_fundings.payments_q2) + accessible_fundings.payments_q3) + accessible_fundings.payments_q4)) AS total FROM (projects LEFT JOIN accessible_fundings ON ((projects.id = accessible_fundings.project_id))) GROUP BY projects.id, projects.donor_id, projects.data_status, accessible_fundings.year, accessible_fundings.currency;


--
-- Name: project_province_count; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW project_province_count AS
    SELECT count(*) AS provinces, t1.project_id FROM (SELECT DISTINCT geo_relevances.province_id, geo_relevances.project_id FROM geo_relevances ORDER BY geo_relevances.project_id, geo_relevances.province_id) t1 GROUP BY t1.project_id;


--
-- Name: projects_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE projects_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: projects_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE projects_id_seq OWNED BY projects.id;


--
-- Name: province_payments; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW province_payments AS
    SELECT t1.province_id, project_payment_totals.year, project_payment_totals.currency, project_payment_totals.data_status, project_payment_totals.donor_id, sum((project_payment_totals.total / (project_province_count.provinces)::double precision)) AS payments FROM (SELECT DISTINCT ON (geo_relevances.project_id, geo_relevances.province_id) geo_relevances.id, geo_relevances.project_id, geo_relevances.province_id, geo_relevances.district_id FROM geo_relevances ORDER BY geo_relevances.project_id, geo_relevances.province_id) t1, project_province_count, project_payment_totals WHERE ((project_province_count.project_id = t1.project_id) AND (project_payment_totals.project_id = t1.project_id)) GROUP BY t1.province_id, project_payment_totals.year, project_payment_totals.currency, project_payment_totals.data_status, project_payment_totals.donor_id;


--
-- Name: provinces; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE provinces (
    id integer NOT NULL,
    name character varying(255),
    code character varying(255)
);


--
-- Name: provinces_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE provinces_id_seq
    START WITH 69
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: provinces_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE provinces_id_seq OWNED BY provinces.id;


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
    layout character varying(255),
    home_path character varying(255)
);


--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE roles_id_seq
    START WITH 3
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE roles_id_seq OWNED BY roles.id;


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
-- Name: sector_details_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sector_details_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: sector_details_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE sector_details_id_seq OWNED BY sector_details.id;


--
-- Name: sector_payments; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW sector_payments AS
    SELECT project_dac_sector_shares.dac_sector_id, project_payment_totals.year, project_payment_totals.currency, project_payment_totals.data_status, project_payment_totals.donor_id, (sum(project_payment_totals.total) * ((100)::double precision / project_dac_sector_shares.amount)) AS payments FROM project_payment_totals, project_dac_sector_shares WHERE (((project_dac_sector_shares.project_id = project_payment_totals.project_id) AND (project_dac_sector_shares.dac_sector_id IS DISTINCT FROM NULL::integer)) AND (project_payment_totals.total IS DISTINCT FROM NULL::double precision)) GROUP BY project_dac_sector_shares.dac_sector_id, project_payment_totals.year, project_payment_totals.currency, project_payment_totals.data_status, project_payment_totals.donor_id, project_dac_sector_shares.amount ORDER BY (sum(project_payment_totals.total) * ((100)::double precision / project_dac_sector_shares.amount)) DESC;


--
-- Name: sector_relevances_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE sector_relevances_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: sector_relevances_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE sector_relevances_id_seq OWNED BY sector_relevances.id;


--
-- Name: settings; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE settings (
    key character varying(255),
    value text
);


--
-- Name: targets; Type: TABLE; Schema: public; Owner: -; Tablespace: 
--

CREATE TABLE targets (
    id integer NOT NULL,
    name text,
    name_es text,
    mdg_id integer
);


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
-- Name: total_odas_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE total_odas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;


--
-- Name: total_odas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE total_odas_id_seq OWNED BY total_odas.id;


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

ALTER SEQUENCE types_of_aid_id_seq OWNED BY aid_modalities.id;


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
    crypted_password character varying(128) DEFAULT ''::character varying NOT NULL,
    salt character varying(128) DEFAULT ''::character varying NOT NULL,
    remember_token character varying(40),
    remember_token_expires_at timestamp without time zone,
    donor_id integer,
    persistence_token character varying(255),
    login_count integer,
    last_request_at timestamp without time zone,
    current_login_at timestamp without time zone,
    last_login_at timestamp without time zone,
    last_login_ip character varying(255),
    current_login_ip character varying(255)
);


--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE users_id_seq
    START WITH 1
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

ALTER TABLE aid_modalities ALTER COLUMN id SET DEFAULT nextval('types_of_aid_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE cofundings ALTER COLUMN id SET DEFAULT nextval('cofundings_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE complex_reports ALTER COLUMN id SET DEFAULT nextval('complex_reports_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE country_strategies ALTER COLUMN id SET DEFAULT nextval('country_strategies_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE crs_sectors ALTER COLUMN id SET DEFAULT nextval('crs_sectors_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE dac_sectors ALTER COLUMN id SET DEFAULT nextval('dac_sectors_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE delegated_cooperations ALTER COLUMN id SET DEFAULT nextval('delegated_cooperations_id_seq'::regclass);


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

ALTER TABLE donor_details ALTER COLUMN id SET DEFAULT nextval('donor_details_id_seq'::regclass);


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

ALTER TABLE sector_relevances ALTER COLUMN id SET DEFAULT nextval('sector_relevances_id_seq'::regclass);


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
-- Name: complex_reports_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY complex_reports
    ADD CONSTRAINT complex_reports_pkey PRIMARY KEY (id);


--
-- Name: country_strategies_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY country_strategies
    ADD CONSTRAINT country_strategies_pkey PRIMARY KEY (id);


--
-- Name: crs_sectors_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY crs_sectors
    ADD CONSTRAINT crs_sectors_pkey PRIMARY KEY (id);


--
-- Name: dac_sectors_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY dac_sectors
    ADD CONSTRAINT dac_sectors_pkey PRIMARY KEY (id);


--
-- Name: delegated_cooperations_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY delegated_cooperations
    ADD CONSTRAINT delegated_cooperations_pkey PRIMARY KEY (id);


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
-- Name: donor_details_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY donor_details
    ADD CONSTRAINT donor_details_pkey PRIMARY KEY (id);


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
-- Name: sector_relevances_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY sector_relevances
    ADD CONSTRAINT sector_relevances_pkey PRIMARY KEY (id);


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
-- Name: types_of_aid_pkey; Type: CONSTRAINT; Schema: public; Owner: -; Tablespace: 
--

ALTER TABLE ONLY aid_modalities
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
-- Name: index_delegated_cooperations_on_delegating_agency_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_delegated_cooperations_on_delegating_agency_id ON delegated_cooperations USING btree (delegating_agency_id);


--
-- Name: index_delegated_cooperations_on_delegating_donor_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_delegated_cooperations_on_delegating_donor_id ON delegated_cooperations USING btree (delegating_donor_id);


--
-- Name: index_delegated_cooperations_on_project_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_delegated_cooperations_on_project_id ON delegated_cooperations USING btree (project_id);


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
-- Name: index_projects_on_delegated_cooperation_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_projects_on_delegated_cooperation_id ON projects USING btree (delegated_cooperation_id);


--
-- Name: index_projects_on_delegating_agency_id; Type: INDEX; Schema: public; Owner: -; Tablespace: 
--

CREATE INDEX index_projects_on_delegating_agency_id ON projects USING btree (delegating_agency_id);


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

INSERT INTO schema_migrations (version) VALUES ('20090103202259');

INSERT INTO schema_migrations (version) VALUES ('20090206131245');

INSERT INTO schema_migrations (version) VALUES ('20090206132239');

INSERT INTO schema_migrations (version) VALUES ('20090209162739');

INSERT INTO schema_migrations (version) VALUES ('20090215205713');

INSERT INTO schema_migrations (version) VALUES ('20090220154516');

INSERT INTO schema_migrations (version) VALUES ('20090220200801');

INSERT INTO schema_migrations (version) VALUES ('20090302153002');

INSERT INTO schema_migrations (version) VALUES ('20090303152739');

INSERT INTO schema_migrations (version) VALUES ('20090306200900');

INSERT INTO schema_migrations (version) VALUES ('20090312142955');

INSERT INTO schema_migrations (version) VALUES ('20090713194312');

INSERT INTO schema_migrations (version) VALUES ('20090715173308');

INSERT INTO schema_migrations (version) VALUES ('20090720135224');

INSERT INTO schema_migrations (version) VALUES ('20090721135224');

INSERT INTO schema_migrations (version) VALUES ('20090827134221');

INSERT INTO schema_migrations (version) VALUES ('20090828051001');

INSERT INTO schema_migrations (version) VALUES ('20090828081121');

INSERT INTO schema_migrations (version) VALUES ('20090905103709');

INSERT INTO schema_migrations (version) VALUES ('20090909101815');