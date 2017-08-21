package com.boilerplate.injection.module;


import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Ensures that Glide's generated API is created for the project.
 */
@GlideModule
public final class ThisAppGlideModule extends AppGlideModule {}