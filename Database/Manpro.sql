--Drop tabel
DROP TABLE IF EXISTS EventVendor;
DROP TABLE IF EXISTS Vendor;
DROP TABLE IF EXISTS JenisVendor;
DROP TABLE IF EXISTS [Event];
DROP TABLE IF EXISTS JenisEvent;
DROP TABLE IF EXISTS Klien;
DROP TABLE IF EXISTS PemilikUsaha;
DROP TABLE IF EXISTS Asisten;
DROP TABLE IF EXISTS [User];

--Tabel user
CREATE TABLE [User] (
    IdUser INT PRIMARY KEY IDENTITY(1,1),
    Nama VARCHAR(100),
    Alamat VARCHAR(255),
    NoTelp VARCHAR(20),
    Username VARCHAR(50),
    [Password] VARCHAR(50)
);

--Tabel asisten
CREATE TABLE Asisten (
	IdAsisten INT PRIMARY KEY,
	FOREIGN KEY (IdAsisten) REFERENCES [User](IdUser)
);

--Tabel pemilik
CREATE TABLE PemilikUsaha (
	IdPemilik INT PRIMARY KEY,
    FOREIGN KEY (IdPemilik) REFERENCES [User](IdUser)
);

--Tabel klien
CREATE TABLE Klien (
    IdKlien INT PRIMARY KEY IDENTITY(1,1),
    Nama VARCHAR(100),
    Alamat VARCHAR(255),
    NoTelp VARCHAR(20),
    Email VARCHAR(100)
);

--Tabel jenisEvent
CREATE TABLE JenisEvent (
    IdJenisEvent INT PRIMARY KEY IDENTITY(1,1),
    Nama VARCHAR(100)
);

--Tabel event
CREATE TABLE [Event] (
    IdEvent INT PRIMARY KEY IDENTITY(1,1),
    Nama VARCHAR(100),
    Tanggal DATE,
    JumlahUndangan INT,
    [Status] VARCHAR(50),
    Budget DECIMAL(15,2),

    IdJenisEvent INT,
    IdKlien INT,
    IdAsisten INT,
	FOREIGN KEY (IdJenisEvent) REFERENCES JenisEvent(IdJenisEvent),
	FOREIGN KEY (IdKlien) REFERENCES Klien(IdKlien),
	FOREIGN KEY (IdAsisten) REFERENCES Asisten(IdAsisten)
);


--Tabel jenis vendor
CREATE TABLE JenisVendor (
    IdJenisVendor INT PRIMARY KEY IDENTITY(1,1),
    Nama VARCHAR(100)
);

--Tabel vendor
CREATE TABLE Vendor (
    IdVendor INT PRIMARY KEY IDENTITY(1,1),
    Nama VARCHAR(100),
    NamaPemilik VARCHAR(100),
    Alamat VARCHAR(255),
    NoTelp VARCHAR(20),
    Harga DECIMAL(15,2),
    IdJenisVendor INT,
    FOREIGN KEY (IdJenisVendor) REFERENCES JenisVendor(IdJenisVendor)
);

--Tabel event vendor
CREATE TABLE EventVendor (
    IdEvent INT,
    IdVendor INT,
    HargaDealing DECIMAL(15,2),

    PRIMARY KEY (IdEvent, IdVendor), 
	FOREIGN KEY (IdEvent) REFERENCES [Event](IdEvent),
	FOREIGN KEY (IdVendor) REFERENCES Vendor(IdVendor)
);


-- Insert data ke tabel Event
-- IdPengguna hanya diisi dengan ID pengguna yang role-nya 'pemilik usaha' (ID: 1, 3, 5)
-- ========================
-- DATA DUMMY EVENT ORGANIZER DATABASE
-- ========================

-- Insert data ke tabel Pengguna
INSERT INTO  [User] (Nama, Alamat, NoTelp, Username, [Password]) VALUES
('Sari Dewi', 'Jl. Dago No. 15, Bandung', '081234567890', 'saridewi', 'password123'),
('Ahmad Fauzi', 'Jl. Braga No. 25, Bandung', '081345678901', 'ahmadfauzi', 'pass456'),
('Rina Sari', 'Jl. Pasteur No. 30, Bandung', '081456789012', 'rinasari', 'mypass789'),
('Budi Santoso', 'Jl. Setiabudi No. 45, Bandung', '081567890123', 'budisantoso', 'budi2023'),
('Maya Indira', 'Jl. Sukajadi No. 12, Bandung', '081678901234', 'mayaindira', 'maya_pass'),
('Dedi Rahman', 'Jl. Cipaganti No. 8, Bandung', '081789012345', 'dedirahman', 'dedi123');

INSERT INTO Asisten (IdAsisten) VALUES (1), (3), (5);
INSERT INTO PemilikUsaha (IdPemilik) VALUES (2), (4), (6);

-- Insert data ke tabel Klien
-- IdPengguna hanya diisi dengan ID pengguna yang role-nya 'asisten' (ID: 2, 4, 6)
INSERT INTO Klien (Nama, Alamat, NoTelp, Email) VALUES
('Lisa Permata', 'Jl. Ciumbuleuit No. 20, Bandung', '082123456789', 'lisa.permata@gmail.com'),
('Yoga Pratama', 'Jl. Riau No. 35, Bandung', '082234567890', 'yoga.pratama@yahoo.com'),
('Dwi Anggraini', 'Jl. Pajajaran No. 50, Bandung', '082345678901', 'dwi.anggraini@hotmail.com'),
('Raka Wijaya', 'Jl. Dipatiukur No. 18, Bandung', '082456789012', 'raka.wijaya@outlook.com'),
('Sinta Maharani', 'Jl. Gatot Subroto No. 22, Bandung', '082567890123', 'sinta.maharani@gmail.com'),
('Andi Kurniawan', 'Jl. Asia Afrika No. 40, Bandung', '082678901234', 'andi.kurniawan@yahoo.com'),
('Fitri Handayani', 'Jl. Merdeka No. 15, Bandung', '082789012345', 'fitri.handayani@gmail.com'),
('Bayu Setiawan', 'Jl. Ahmad Yani No. 28, Bandung', '082890123456', 'bayu.setiawan@gmail.com');


-- Insert data ke tabel JenisEvent
INSERT INTO JenisEvent (Nama) VALUES
('Pernikahan'),
('Ulang Tahun'),
('Corporate Event'),
('Seminar'),
('Workshop'),
('Grand Opening'),
('Gathering'),
('Pameran');

-- Insert data ke tabel JenisVendor
INSERT INTO JenisVendor (Nama) VALUES
('Catering'),
('Dekorasi'),
('Fotografi'),
('Musik & Hiburan'),
('Wedding Organizer'),
('Sound System'),
('Transportasi'),
('Venue'),
('MC & Host'),
('Florist');

-- Insert data ke tabel Vendor
INSERT INTO Vendor (Nama, NamaPemilik, Alamat, NoTelp, Harga, IdJenisVendor) VALUES
('Bandung Catering Deluxe', 'Hendra Wijaya', 'Jl. Buah Batu No. 100, Bandung', '022-7531234', 75000.00, 1),
('Elegant Decoration', 'Sari Kumala', 'Jl. Cihampelas No. 85, Bandung', '022-2031567', 2500000.00, 2),
('Capture Moment Studio', 'Ridwan Kamil', 'Jl. Dago No. 55, Bandung', '022-2501890', 3500000.00, 3),
('Harmoni Music Entertainment', 'Diana Puspita', 'Jl. Pasteur No. 40, Bandung', '022-2012345', 1800000.00, 4),
('Perfect Wedding Planner', 'Eka Sari', 'Jl. Setiabudi No. 25, Bandung', '022-2030987', 5000000.00, 5),
('Pro Sound System', 'Agus Salim', 'Jl. Sukajadi No. 30, Bandung', '022-2041234', 800000.00, 6),
('Royal Transportation', 'Bambang Sutrisno', 'Jl. Gatot Subroto No. 60, Bandung', '022-7540876', 1200000.00, 7),
('Grand Ballroom Venue', 'Dewi Lestari', 'Jl. Asia Afrika No. 88, Bandung', '022-4230987', 8000000.00, 8),
('Master of Ceremony Pro', 'Rizki Pratama', 'Jl. Braga No. 45, Bandung', '022-4201234', 1000000.00, 9),
('Blossom Florist', 'Nina Anggraeni', 'Jl. Cipaganti No. 20, Bandung', '022-2031098', 1500000.00, 10),
('Spice Garden Catering', 'Lukman Hakim', 'Jl. Riau No. 75, Bandung', '022-7532109', 65000.00, 1),
('Dreamy Decor', 'Putri Maharani', 'Jl. Dipatiukur No. 35, Bandung', '022-2032456', 3000000.00, 2),
('Melody Entertainment', 'Faisal Rahman', 'Jl. Pajajaran No. 15, Bandung', '022-2013456', 2200000.00, 4),
('Crystal Sound', 'Hadi Kusuma', 'Jl. Merdeka No. 50, Bandung', '022-2042567', 900000.00, 6),
('Paradise Venue Hall', 'Lina Marlina', 'Jl. Ahmad Yani No. 120, Bandung', '022-4231876', 7500000.00, 8);

-- Insert data ke tabel Event
INSERT INTO [Event] (Nama, Tanggal, JumlahUndangan, Budget, IdJenisEvent, IdKlien, IdAsisten, [Status]) VALUES
('Pernikahan Lisa & Yoga', '2024-12-15', 300, 150000000.00, 1, 1, 1, 'On Progress'),
('Ulang Tahun Dwi ke-30', '2024-11-20', 50, 25000000.00, 2, 3, 3, 'On Progress'),
('Corporate Gathering PT. Maju', '2024-10-25', 150, 75000000.00, 3, 4, 5, 'On Progress'),
('Seminar Digital Marketing', '2024-11-10', 100, 30000000.00, 4, 5, 3, 'On Progress'),
('Workshop Photography', '2024-12-05', 40, 15000000.00, 5, 6, 1, 'On Progress'),
('Grand Opening Cafe Sinta', '2024-11-30', 200, 80000000.00, 6, 5, 5, 'On Progress'),
('Family Gathering Kurniawan', '2024-12-20', 80, 35000000.00, 7, 6, 1, 'On Progress'),
('Pameran Produk Lokal', '2024-11-15', 500, 120000000.00, 8, 7, 3, 'On Progress'),
('Pernikahan Fitri & Bayu', '2024-12-25', 250, 130000000.00, 1, 7, 5, 'On Progress'),
('Birthday Party Raka', '2024-11-05', 60, 20000000.00, 2, 4, 5, 'On Progress');

-- Insert data ke tabel EventVendor
INSERT INTO EventVendor (IdEvent, IdVendor, HargaDealing) VALUES
-- Pernikahan Lisa & Yoga
(1, 1, 22500000.00), -- Catering (300 orang x 75rb)
(1, 2, 2300000.00),  -- Dekorasi
(1, 3, 3200000.00),  -- Fotografi
(1, 4, 1600000.00),  -- Musik
(1, 5, 4800000.00),  -- Wedding Organizer
(1, 8, 7500000.00),  -- Venue
(1, 10, 1400000.00), -- Florist

-- Ulang Tahun Dwi ke-30
(2, 11, 3250000.00), -- Catering (50 orang x 65rb)
(2, 12, 2800000.00), -- Dekorasi
(2, 13, 2000000.00), -- Hiburan
(2, 14, 850000.00),  -- Sound System

-- Corporate Gathering PT. Maju
(3, 1, 11250000.00), -- Catering (150 orang x 75rb)
(3, 2, 2200000.00),  -- Dekorasi
(3, 6, 750000.00),   -- Sound System
(3, 15, 7000000.00), -- Venue
(3, 9, 950000.00),   -- MC

-- Seminar Digital Marketing
(4, 11, 6500000.00), -- Catering (100 orang x 65rb)
(4, 6, 780000.00),   -- Sound System
(4, 15, 7200000.00), -- Venue
(4, 9, 900000.00),   -- MC

-- Workshop Photography
(5, 11, 2600000.00), -- Catering (40 orang x 65rb)
(5, 14, 800000.00),  -- Sound System
(5, 9, 850000.00),   -- MC

-- Grand Opening Cafe Sinta
(6, 1, 15000000.00), -- Catering (200 orang x 75rb)
(6, 2, 2400000.00),  -- Dekorasi
(6, 13, 2100000.00), -- Hiburan
(6, 6, 800000.00),   -- Sound System
(6, 10, 1300000.00), -- Florist

-- Family Gathering Kurniawan
(7, 11, 5200000.00), -- Catering (80 orang x 65rb)
(7, 12, 2600000.00), -- Dekorasi
(7, 13, 1900000.00), -- Hiburan
(7, 14, 820000.00),  -- Sound System

-- Pameran Produk Lokal
(8, 1, 37500000.00), -- Catering (500 orang x 75rb)
(8, 2, 2600000.00),  -- Dekorasi
(8, 6, 900000.00),   -- Sound System
(8, 8, 8200000.00),  -- Venue
(8, 9, 1100000.00),  -- MC

-- Pernikahan Fitri & Bayu
(9, 1, 18750000.00), -- Catering (250 orang x 75rb)
(9, 2, 2500000.00),  -- Dekorasi
(9, 3, 3300000.00),  -- Fotografi
(9, 4, 1700000.00),  -- Musik
(9, 5, 4700000.00),  -- Wedding Organizer
(9, 8, 7800000.00),  -- Venue
(9, 10, 1350000.00), -- Florist

-- Birthday Party Raka
(10, 11, 3900000.00), -- Catering (60 orang x 65rb)
(10, 12, 2700000.00), -- Dekorasi
(10, 13, 2000000.00), -- Hiburan
(10, 14, 850000.00);  -- Sound System
-- ========================
-- QUERY UNTUK VERIFIKASI DATA
-- ========================

-- Menampilkan total event per jenis
SELECT je.Nama as JenisEvent, COUNT(e.IdEvent) as TotalEvent
FROM JenisEvent je
LEFT JOIN [Event] e ON je.IdJenisEvent = e.IdJenisEvent
GROUP BY je.Nama
ORDER BY TotalEvent DESC;

-- Menampilkan vendor yang paling sering digunakan
SELECT v.Nama as NamaVendor, jv.Nama as JenisVendor, COUNT(ev.IdVendor) as TotalPenggunaan
FROM Vendor v
JOIN JenisVendor jv ON v.IdJenisVendor = jv.IdJenisVendor
LEFT JOIN EventVendor ev ON v.IdVendor = ev.IdVendor
GROUP BY v.Nama, jv.Nama
ORDER BY TotalPenggunaan DESC;

-- Menampilkan event dengan budget terbesar
SELECT e.Nama as NamaEvent, k.Nama as NamaKlien, je.Nama as JenisEvent, 
       e.Budget, e.Tanggal
FROM [Event] e
JOIN Klien k ON e.IdKlien = k.IdKlien
JOIN JenisEvent je ON e.IdJenisEvent = je.IdJenisEvent
ORDER BY e.Budget DESC;


SELECT * FROM [User]

SELECT * FROM Asisten

SELECT * FROM PemilikUsaha

SELECT * FROM Klien

SELECT * FROM JenisEvent

SELECT * FROM [Event]

SELECT * FROM JenisVendor

SELECT * FROM Vendor

SELECT * FROM EventVendor


--Ambil data klient yang diambil Asisten dengan id 1
SELECT DISTINCT k.*
FROM Klien k
JOIN [Event] e ON k.IdKlien = e.IdKlien
WHERE e.IdAsisten = 1;

--Ambil data event yang diambil Asisten dengan id 3
SELECT e.*
FROM [Event] e
WHERE e.IdAsisten = 3;

--Ambil data klien beserta keterangan eventnya milik Asisten dengan id 5
SELECT u.Nama AS NamaAsisten, k.Nama AS NamaKlien, e.Nama AS NamaEvent, e.Tanggal, e.JumlahUndangan, e.Budget, je.Nama AS JenisEvent
FROM Asisten a
JOIN [User] u ON a.IdAsisten = u.IdUser
JOIN [Event] e ON e.IdAsisten = a.IdAsisten
JOIN Klien k ON k.IdKlien = e.IdKlien
JOIN JenisEvent je ON je.IdJenisEvent = e.IdJenisEvent
WHERE a.IdAsisten = 5
ORDER BY k.IdKlien, e.IdEvent;
