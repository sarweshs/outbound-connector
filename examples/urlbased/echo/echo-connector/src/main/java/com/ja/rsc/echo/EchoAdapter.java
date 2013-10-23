/*
Copyright 2013 Thomas Scheuchzer, java-adventures.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.ja.rsc.echo;

import javax.resource.spi.Connector;
import javax.resource.spi.TransactionSupport;

import com.ja.rsc.AbstractAdapter;

@Connector(reauthenticationSupport = false, transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction)
public class EchoAdapter extends AbstractAdapter {

}
