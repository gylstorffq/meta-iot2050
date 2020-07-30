#
# Copyright (c) Siemens AG, 2020
#
# Authors:
#  Quirin Gylstorff <quirin.gylstorff@siemens.com>
#
# This file is subject to the terms and conditions of the MIT License.  See
# COPYING.MIT file in the top-level directory.
#

DESCRIPTION = "swupdate utility for software updates"
HOMEPAGE= "https://github.com/sbabic/swupdate"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${LAYERDIR_isar}/licenses/COPYING.GPLv2;md5=751419260aa954499f7abaabaa882bbe"


KFEATURE += "u-boot"
U_BOOT = "u-boot-iot2050"
BOOTLOADER = "u-boot"

SRC_URI = "git://github.com/sbabic/swupdate.git;branch=master;protocol=https"
SRCREV = "1a6dfbb5a0be978ac1a159758e278ab4d44167e2"
PV = "2020.4-git+isar"

DEFCONFIG := "swupdate_defconfig"

SRC_URI += "file://debian \
            file://${DEFCONFIG} \
            file://swupdate.cfg"

DEPENDS += "libubootenv"

DEBIAN_DEPENDS = "${shlibs:Depends}, ${misc:Depends}"
BUILD_DEB_DEPENDS += "libubootenv"
inherit dpkg
inherit swupdate-config

S = "${WORKDIR}/git"

TEMPLATE_FILES = "debian/changelog.tmpl debian/control.tmpl debian/rules.tmpl"
TEMPLATE_VARS += "BUILD_DEB_DEPENDS DEFCONFIG DEBIAN_DEPENDS"

do_prepare_build() {
        DEBDIR=${S}/debian
        cp -R ${WORKDIR}/debian ${S}

        install -m 0644 ${WORKDIR}/${PN}.cfg ${S}/${PN}.cfg
        install -m 0644 ${WORKDIR}/${DEFCONFIG}.gen ${S}/configs/${DEFCONFIG}

        if ! grep -q "configs/${DEFCONFIG}" ${S}/.gitignore
        then
                echo "configs/${DEFCONFIG}" >> ${S}/.gitignore
        fi
}
