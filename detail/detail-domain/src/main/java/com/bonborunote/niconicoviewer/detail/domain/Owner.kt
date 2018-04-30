package com.bonborunote.niconicoviewer.detail.domain

import com.bonborunote.niconicoviewer.common.Entity

class Owner(
  id: OwnerId,
  val name: String,
  val thumb: String
) : Entity<OwnerId>(id)
