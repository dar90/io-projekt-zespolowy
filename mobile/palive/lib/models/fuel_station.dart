class FuelStation {
  final int id;
  final String name;
  final double latitude;
  final double longitude;
  final String brand;
  final bool verified;
  final List<String> services;
  final String city;
  final String street;
  final String plotNumber;
  final String logoUrl;

  FuelStation({
          required this.id,
          required this.latitude,
          required this.longitude,
          required this.name,
          required this.brand,
          required this.verified,
          required this.services,
          required this.city,
          required this.street,
          required this.plotNumber,
          required this.logoUrl
  });

}