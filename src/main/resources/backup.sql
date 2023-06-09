PGDMP         !        	        {         
   headHunter    15.2    15.2                0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    16564 
   headHunter    DATABASE     �   CREATE DATABASE "headHunter" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';
    DROP DATABASE "headHunter";
                postgres    false            �            1259    16565    contract    TABLE     �   CREATE TABLE public.contract (
    number integer NOT NULL,
    "nameVillain" character varying(32) NOT NULL,
    "nameMinion" character varying(32) NOT NULL,
    start date DEFAULT '2023-05-05'::date
);
    DROP TABLE public.contract;
       public         heap    postgres    false            �            1259    16569    contract_number_seq    SEQUENCE     �   CREATE SEQUENCE public.contract_number_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.contract_number_seq;
       public          postgres    false    214                       0    0    contract_number_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.contract_number_seq OWNED BY public.contract.number;
          public          postgres    false    215            �            1259    16570    minion    TABLE     �   CREATE TABLE public.minion (
    name character varying(32) NOT NULL,
    weakness character varying(32) NOT NULL,
    "eyeCount" smallint DEFAULT 2,
    CONSTRAINT "checkEyeCount" CHECK (("eyeCount" >= 0))
);
    DROP TABLE public.minion;
       public         heap    postgres    false            �            1259    16575    villain    TABLE     �   CREATE TABLE public.villain (
    name character varying(32) NOT NULL,
    nickname character varying(64) NOT NULL,
    evilness smallint DEFAULT 0,
    CONSTRAINT "evilnessCheck" CHECK (((evilness >= 0) AND (evilness <= 10)))
);
    DROP TABLE public.villain;
       public         heap    postgres    false            m           2604    16580    contract number    DEFAULT     r   ALTER TABLE ONLY public.contract ALTER COLUMN number SET DEFAULT nextval('public.contract_number_seq'::regclass);
 >   ALTER TABLE public.contract ALTER COLUMN number DROP DEFAULT;
       public          postgres    false    215    214                      0    16565    contract 
   TABLE DATA           N   COPY public.contract (number, "nameVillain", "nameMinion", start) FROM stdin;
    public          postgres    false    214                    0    16570    minion 
   TABLE DATA           <   COPY public.minion (name, weakness, "eyeCount") FROM stdin;
    public          postgres    false    216   �                 0    16575    villain 
   TABLE DATA           ;   COPY public.villain (name, nickname, evilness) FROM stdin;
    public          postgres    false    217                     0    0    contract_number_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.contract_number_seq', 40, true);
          public          postgres    false    215            t           2606    16582    contract contract_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.contract
    ADD CONSTRAINT contract_pkey PRIMARY KEY (number);
 @   ALTER TABLE ONLY public.contract DROP CONSTRAINT contract_pkey;
       public            postgres    false    214            x           2606    16584    minion minion_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.minion
    ADD CONSTRAINT minion_pkey PRIMARY KEY (name);
 <   ALTER TABLE ONLY public.minion DROP CONSTRAINT minion_pkey;
       public            postgres    false    216            v           2606    16586    contract uniqueContract 
   CONSTRAINT     k   ALTER TABLE ONLY public.contract
    ADD CONSTRAINT "uniqueContract" UNIQUE ("nameVillain", "nameMinion");
 C   ALTER TABLE ONLY public.contract DROP CONSTRAINT "uniqueContract";
       public            postgres    false    214    214            z           2606    16588    villain vilian_pkey 
   CONSTRAINT     S   ALTER TABLE ONLY public.villain
    ADD CONSTRAINT vilian_pkey PRIMARY KEY (name);
 =   ALTER TABLE ONLY public.villain DROP CONSTRAINT vilian_pkey;
       public            postgres    false    217            {           2606    16589    contract minionFK    FK CONSTRAINT     z   ALTER TABLE ONLY public.contract
    ADD CONSTRAINT "minionFK" FOREIGN KEY ("nameMinion") REFERENCES public.minion(name);
 =   ALTER TABLE ONLY public.contract DROP CONSTRAINT "minionFK";
       public          postgres    false    3192    216    214            |           2606    16594    contract vilianFK    FK CONSTRAINT     |   ALTER TABLE ONLY public.contract
    ADD CONSTRAINT "vilianFK" FOREIGN KEY ("nameVillain") REFERENCES public.villain(name);
 =   ALTER TABLE ONLY public.contract DROP CONSTRAINT "vilianFK";
       public          postgres    false    217    214    3194               �   x�U�A
�0E��)z�����w�a��]���UPRE���F��Hx��Ϝ
J)7xSF9j~dƅ.:�1�$P�C�I�H�3�΂>l�ł^��������ͫ2{t|M�u�&�
5&zh�lڌaQ�[��������k���n         G  x�mRYN�0�vN� ��p�$ ji�WY� �w��r[b���F���X�"+{�6��18,�1�kw���J��RH��l��3�� ^�s���Qa�n��V��3��!P�@�`�,��$�B�"�#�F�V,�d���(bN}˭�W82j+57:���~��D�e�,������z�6r&j"�ф���Q3n��A�)�x���)u��a�F�Y&l��qA���]hϥ�Y=�:�u<���wx�+uc��7��!�R;<�9G,oa�4Pw���S�&"ռ,r�[���>���-�я� R;����騪���u�)�����$˲x��         �   x�5O[
�0�NN�TQ�.��_E�� A����4�
�7r� �Hfvfv����18"�G�����fjqB/��[ԒI��w*9��I�-x�;���Lr)�t�0O'���F��>	
4��UBV�fF��n	���:|���b7:;�+�U<A�*�G�3���L�v9��� ̚�R     