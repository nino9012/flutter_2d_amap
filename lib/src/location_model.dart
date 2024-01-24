class LocationResult{

  int? altitude;

  int? speed;

  int? bearing;

  String? citycode;

  String? adcode;

  String? country;

  String? province;

  String? city;

  String? district;

  String? road;

  String? street;

  String? number;

  String? poiname;

  int? errorCode;

  String? errorInfo;

  int? locationType;

  String? locationDetail;

  String? aoiname;

  String? address;

  String? poiid;

  String? floor;

  String? description;

  int? time;

  String? provider;

  double? lon;

  double? lat;

  int? accuracy;

  bool? isOffset;

  bool? isFixLastLocation;

  String? coordType;

  LocationResult(this.altitude, this.speed, this.bearing, this.citycode, this.adcode,
      this.country, this.province, this.city, this.district, this.road,
      this.street, this.number, this.poiname, this.errorCode, this.errorInfo,
      this.locationType, this.locationDetail, this.aoiname, this.address,
      this.poiid, this.floor, this.description, this.time, this.provider,
      this.lon, this.lat, this.accuracy, this.isOffset, this.isFixLastLocation,
      this.coordType);

  LocationResult.fromJsonMap(Map<String, dynamic> map):
        altitude = map['altitude'] as int?,
        speed = map['speed'] as int?,
        bearing = map['bearing'] as int?,
        citycode = map['citycode'] as String?,
        adcode = map['adcode'] as String?,
        country = map['country'] as String?,
        province = map['province'] as String?,
        city = map['city'] as String?,
        district = map['district'] as String?,
        road = map['road'] as String?,
        street = map['street'] as String?,
        number = map['number'] as String?,
        poiname = map['poiname'] as String?,
        errorCode = map['errorCode'] as int?,
        errorInfo = map['errorInfo'] as String?,
        locationType = map['locationType'] as int?,
        locationDetail = map['locationDetail'] as String?,
        aoiname = map['aoiname'] as String?,
        address = map['address'] as String?,
        poiid = map['poiid'] as String?,
        floor = map['floor'] as String?,
        description = map['description'] as String?,
        time = map['time'] as int?,
        provider = map['provider'] as String?,
        lon = map['lon'] as double?,
        lat = map['lat'] as double?,
        accuracy = map['accuracy'] as int?,
        isOffset = map['isOffset'] as bool?,
        isFixLastLocation = map['isFixLastLocation'] as bool?,
        coordType = map['coordType'] as String?;
}