package com.bonborunote.niconicoviewer.common.models

import com.bonborunote.niconicoviewer.common.Entity

class Channel(
  id: ChannelId,
  val name: String,
  val thumb: String
) : Entity<ChannelId>(id)
