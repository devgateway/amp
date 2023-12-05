<%@ taglib uri="/src/main/webapp/WEB-INF/tiles.tld" prefix="tiles" %>



  <html>
    <head>
      <title>dgMarket - Development Gateway Market</title>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <meta name="description" content="Development Gateway market for tenders and procurement opportunities in all countries - from international development banks and government agencies worldwide. Provides free summaries of tender notices and free e-mail alerts. Created by the World Bank, UNDP and other organizations."/>
      <meta name="keywords" content="tender, procurement, purchasing, business opportunities, World Bank, ADB, EBRD, Asian Development Bank, AfDB, SIMAP, TED, CPV, UN, EU, consulting, e-mail alerts, email alerts, tender alerts, invitation to bid, consultants, contract award, international, competitive bidding, ICB, bid, bids, contracts, government, agency, project, development business, expressions of interest, prequalification, IFB"/>
      <link href="jsp/eproc/images/styles.css" type="text/css" rel="stylesheet"/>

        <script language="Javascript">
          <!--
          var OK_to_leave = "yes";
          //-->
        </script>

    </head>

    <body  onBeforeUnload='if(OK_to_leave=="no") {return "All changes made by you on this page will be lost!"}'>
      <table cellpadding="0" cellspacing="0" width="100%" border="0">
        <tr>

			<!-- dacon-header.jsp -->
			<tiles:insert attribute="header" />

        </tr>


        <tr>




            <td valign="top" width=160 class="background">

				<tiles:insert attribute="leftmenu" />


            </td>


            <td width="1">
            	&nbsp;
            </td>




            <td valign="top" colspan="2">

				<table width="100%" cellspacing="0"  cellpadding="0">
				            <tr>
								<tiles:insert attribute="menuheader" />


				         	</tr>


				         	<tr>
				         		<td height="5"></td>
				         	</tr>

				         	<tr>

								<tiles:insert attribute="body" />

				        	</tr>

				</table>



			</td>

		</tr>

        <tr>

				<tiles:insert attribute="footer" />


        </tr>

</table>

</body>
</html>
