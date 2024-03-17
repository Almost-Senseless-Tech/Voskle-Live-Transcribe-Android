/*
* Copyright Gabriel Aumala (c) 2018
*
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
*
*    - Redistributions of source code must retain the above copyright
*       notice, this list of conditions and the following disclaimer.
*
*     - Redistributions in binary form must reproduce the above
*       copyright notice, this list of conditions and the following
*       disclaimer in the documentation and/or other materials provided
*       with the distribution.
*
*     - Neither the name of Author name here nor the names of other
*       contributors may be used to endorse or promote products derived
*       from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
* "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
* LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
* A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
* OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
* SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
* LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
* DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
* THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package tech.almost_senseless.voskle.util

import java.io.IOException
import java.io.InputStream

class ObservableInputStream(private val wrapped: InputStream,
                            private val onBytesRead: (Long) -> Unit): InputStream() {
    private var bytesRead: Long = 0

    @Throws(IOException::class)
    override fun read(): Int {
        val res = wrapped.read()
        if (res > -1) {
            bytesRead++
        }
        onBytesRead(bytesRead)
        return res
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray): Int {
        val res = wrapped.read(b)
        if (res > -1) {
            bytesRead += res
            onBytesRead(bytesRead)
        }
        return res
    }

    @Throws(IOException::class)
    override fun read(b: ByteArray, off: Int, len: Int): Int {
        val res = wrapped.read(b, off, len)
        if (res > -1) {
            bytesRead += res
            onBytesRead(bytesRead)
        }
        return res
    }

    @Throws(IOException::class)
    override fun skip(n: Long): Long {
        val res = wrapped.skip(n)
        if (res > -1) {
            bytesRead += res
            onBytesRead(bytesRead)
        }
        return res
    }

    @Throws(IOException::class)
    override fun available(): Int {
        return wrapped.available()
    }

    override fun markSupported(): Boolean {
        return wrapped.markSupported()
    }

    override fun mark(readlimit: Int) {
        wrapped.mark(readlimit)
    }

    @Throws(IOException::class)
    override fun reset() {
        wrapped.reset()
    }

    @Throws(IOException::class)
    override fun close() {
        wrapped.close()
    }
}