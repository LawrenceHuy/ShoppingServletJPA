-- Chỉ cần tạo database rỗng. Các bảng (Users, categories, videos) do Hibernate tự tạo khi chạy.
IF DB_ID('ShoppingServletJPA') IS NULL CREATE DATABASE ShoppingServletJPA;
GO
